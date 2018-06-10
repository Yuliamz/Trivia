package com.trivia.web.rest;

import com.trivia.TriviaApp;

import com.trivia.domain.ClientAnswer;
import com.trivia.repository.ClientAnswerRepository;
import com.trivia.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.trivia.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClientAnswerResource REST controller.
 *
 * @see ClientAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class ClientAnswerResourceIntTest {

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    @Autowired
    private ClientAnswerRepository clientAnswerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClientAnswerMockMvc;

    private ClientAnswer clientAnswer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientAnswerResource clientAnswerResource = new ClientAnswerResource(clientAnswerRepository);
        this.restClientAnswerMockMvc = MockMvcBuilders.standaloneSetup(clientAnswerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientAnswer createEntity(EntityManager em) {
        ClientAnswer clientAnswer = new ClientAnswer()
            .correct(DEFAULT_CORRECT)
            .time(DEFAULT_TIME);
        return clientAnswer;
    }

    @Before
    public void initTest() {
        clientAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientAnswer() throws Exception {
        int databaseSizeBeforeCreate = clientAnswerRepository.findAll().size();

        // Create the ClientAnswer
        restClientAnswerMockMvc.perform(post("/api/client-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientAnswer)))
            .andExpect(status().isCreated());

        // Validate the ClientAnswer in the database
        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        ClientAnswer testClientAnswer = clientAnswerList.get(clientAnswerList.size() - 1);
        assertThat(testClientAnswer.isCorrect()).isEqualTo(DEFAULT_CORRECT);
        assertThat(testClientAnswer.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createClientAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientAnswerRepository.findAll().size();

        // Create the ClientAnswer with an existing ID
        clientAnswer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientAnswerMockMvc.perform(post("/api/client-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the ClientAnswer in the database
        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCorrectIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientAnswerRepository.findAll().size();
        // set the field null
        clientAnswer.setCorrect(null);

        // Create the ClientAnswer, which fails.

        restClientAnswerMockMvc.perform(post("/api/client-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientAnswer)))
            .andExpect(status().isBadRequest());

        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientAnswerRepository.findAll().size();
        // set the field null
        clientAnswer.setTime(null);

        // Create the ClientAnswer, which fails.

        restClientAnswerMockMvc.perform(post("/api/client-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientAnswer)))
            .andExpect(status().isBadRequest());

        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientAnswers() throws Exception {
        // Initialize the database
        clientAnswerRepository.saveAndFlush(clientAnswer);

        // Get all the clientAnswerList
        restClientAnswerMockMvc.perform(get("/api/client-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void getClientAnswer() throws Exception {
        // Initialize the database
        clientAnswerRepository.saveAndFlush(clientAnswer);

        // Get the clientAnswer
        restClientAnswerMockMvc.perform(get("/api/client-answers/{id}", clientAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientAnswer.getId().intValue()))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingClientAnswer() throws Exception {
        // Get the clientAnswer
        restClientAnswerMockMvc.perform(get("/api/client-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientAnswer() throws Exception {
        // Initialize the database
        clientAnswerRepository.saveAndFlush(clientAnswer);
        int databaseSizeBeforeUpdate = clientAnswerRepository.findAll().size();

        // Update the clientAnswer
        ClientAnswer updatedClientAnswer = clientAnswerRepository.findOne(clientAnswer.getId());
        // Disconnect from session so that the updates on updatedClientAnswer are not directly saved in db
        em.detach(updatedClientAnswer);
        updatedClientAnswer
            .correct(UPDATED_CORRECT)
            .time(UPDATED_TIME);

        restClientAnswerMockMvc.perform(put("/api/client-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClientAnswer)))
            .andExpect(status().isOk());

        // Validate the ClientAnswer in the database
        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeUpdate);
        ClientAnswer testClientAnswer = clientAnswerList.get(clientAnswerList.size() - 1);
        assertThat(testClientAnswer.isCorrect()).isEqualTo(UPDATED_CORRECT);
        assertThat(testClientAnswer.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingClientAnswer() throws Exception {
        int databaseSizeBeforeUpdate = clientAnswerRepository.findAll().size();

        // Create the ClientAnswer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClientAnswerMockMvc.perform(put("/api/client-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientAnswer)))
            .andExpect(status().isCreated());

        // Validate the ClientAnswer in the database
        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClientAnswer() throws Exception {
        // Initialize the database
        clientAnswerRepository.saveAndFlush(clientAnswer);
        int databaseSizeBeforeDelete = clientAnswerRepository.findAll().size();

        // Get the clientAnswer
        restClientAnswerMockMvc.perform(delete("/api/client-answers/{id}", clientAnswer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientAnswer> clientAnswerList = clientAnswerRepository.findAll();
        assertThat(clientAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientAnswer.class);
        ClientAnswer clientAnswer1 = new ClientAnswer();
        clientAnswer1.setId(1L);
        ClientAnswer clientAnswer2 = new ClientAnswer();
        clientAnswer2.setId(clientAnswer1.getId());
        assertThat(clientAnswer1).isEqualTo(clientAnswer2);
        clientAnswer2.setId(2L);
        assertThat(clientAnswer1).isNotEqualTo(clientAnswer2);
        clientAnswer1.setId(null);
        assertThat(clientAnswer1).isNotEqualTo(clientAnswer2);
    }
}
