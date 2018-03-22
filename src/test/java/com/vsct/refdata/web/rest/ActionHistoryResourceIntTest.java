package com.vsct.refdata.web.rest;

import com.vsct.refdata.RefDataGeneratorApp;

import com.vsct.refdata.domain.ActionHistory;
import com.vsct.refdata.repository.ActionHistoryRepository;
import com.vsct.refdata.service.ActionHistoryService;
import com.vsct.refdata.service.dto.ActionHistoryDTO;
import com.vsct.refdata.service.mapper.ActionHistoryMapper;
import com.vsct.refdata.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.vsct.refdata.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ActionHistoryResource REST controller.
 *
 * @see ActionHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RefDataGeneratorApp.class)
public class ActionHistoryResourceIntTest {

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ActionHistoryRepository actionHistoryRepository;

    @Autowired
    private ActionHistoryMapper actionHistoryMapper;

    @Autowired
    private ActionHistoryService actionHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActionHistoryMockMvc;

    private ActionHistory actionHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActionHistoryResource actionHistoryResource = new ActionHistoryResource(actionHistoryService);
        this.restActionHistoryMockMvc = MockMvcBuilders.standaloneSetup(actionHistoryResource)
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
    public static ActionHistory createEntity(EntityManager em) {
        ActionHistory actionHistory = new ActionHistory()
            .user(DEFAULT_USER)
            .message(DEFAULT_MESSAGE)
            .date(DEFAULT_DATE);
        return actionHistory;
    }

    @Before
    public void initTest() {
        actionHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createActionHistory() throws Exception {
        int databaseSizeBeforeCreate = actionHistoryRepository.findAll().size();

        // Create the ActionHistory
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(actionHistory);
        restActionHistoryMockMvc.perform(post("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ActionHistory in the database
        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ActionHistory testActionHistory = actionHistoryList.get(actionHistoryList.size() - 1);
        assertThat(testActionHistory.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testActionHistory.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testActionHistory.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createActionHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actionHistoryRepository.findAll().size();

        // Create the ActionHistory with an existing ID
        actionHistory.setId(1L);
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(actionHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionHistoryMockMvc.perform(post("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActionHistory in the database
        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = actionHistoryRepository.findAll().size();
        // set the field null
        actionHistory.setUser(null);

        // Create the ActionHistory, which fails.
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(actionHistory);

        restActionHistoryMockMvc.perform(post("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = actionHistoryRepository.findAll().size();
        // set the field null
        actionHistory.setMessage(null);

        // Create the ActionHistory, which fails.
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(actionHistory);

        restActionHistoryMockMvc.perform(post("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = actionHistoryRepository.findAll().size();
        // set the field null
        actionHistory.setDate(null);

        // Create the ActionHistory, which fails.
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(actionHistory);

        restActionHistoryMockMvc.perform(post("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActionHistories() throws Exception {
        // Initialize the database
        actionHistoryRepository.saveAndFlush(actionHistory);

        // Get all the actionHistoryList
        restActionHistoryMockMvc.perform(get("/api/action-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getActionHistory() throws Exception {
        // Initialize the database
        actionHistoryRepository.saveAndFlush(actionHistory);

        // Get the actionHistory
        restActionHistoryMockMvc.perform(get("/api/action-histories/{id}", actionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actionHistory.getId().intValue()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActionHistory() throws Exception {
        // Get the actionHistory
        restActionHistoryMockMvc.perform(get("/api/action-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActionHistory() throws Exception {
        // Initialize the database
        actionHistoryRepository.saveAndFlush(actionHistory);
        int databaseSizeBeforeUpdate = actionHistoryRepository.findAll().size();

        // Update the actionHistory
        ActionHistory updatedActionHistory = actionHistoryRepository.findOne(actionHistory.getId());
        // Disconnect from session so that the updates on updatedActionHistory are not directly saved in db
        em.detach(updatedActionHistory);
        updatedActionHistory
            .user(UPDATED_USER)
            .message(UPDATED_MESSAGE)
            .date(UPDATED_DATE);
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(updatedActionHistory);

        restActionHistoryMockMvc.perform(put("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ActionHistory in the database
        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeUpdate);
        ActionHistory testActionHistory = actionHistoryList.get(actionHistoryList.size() - 1);
        assertThat(testActionHistory.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testActionHistory.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testActionHistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingActionHistory() throws Exception {
        int databaseSizeBeforeUpdate = actionHistoryRepository.findAll().size();

        // Create the ActionHistory
        ActionHistoryDTO actionHistoryDTO = actionHistoryMapper.toDto(actionHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActionHistoryMockMvc.perform(put("/api/action-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ActionHistory in the database
        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActionHistory() throws Exception {
        // Initialize the database
        actionHistoryRepository.saveAndFlush(actionHistory);
        int databaseSizeBeforeDelete = actionHistoryRepository.findAll().size();

        // Get the actionHistory
        restActionHistoryMockMvc.perform(delete("/api/action-histories/{id}", actionHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActionHistory> actionHistoryList = actionHistoryRepository.findAll();
        assertThat(actionHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionHistory.class);
        ActionHistory actionHistory1 = new ActionHistory();
        actionHistory1.setId(1L);
        ActionHistory actionHistory2 = new ActionHistory();
        actionHistory2.setId(actionHistory1.getId());
        assertThat(actionHistory1).isEqualTo(actionHistory2);
        actionHistory2.setId(2L);
        assertThat(actionHistory1).isNotEqualTo(actionHistory2);
        actionHistory1.setId(null);
        assertThat(actionHistory1).isNotEqualTo(actionHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionHistoryDTO.class);
        ActionHistoryDTO actionHistoryDTO1 = new ActionHistoryDTO();
        actionHistoryDTO1.setId(1L);
        ActionHistoryDTO actionHistoryDTO2 = new ActionHistoryDTO();
        assertThat(actionHistoryDTO1).isNotEqualTo(actionHistoryDTO2);
        actionHistoryDTO2.setId(actionHistoryDTO1.getId());
        assertThat(actionHistoryDTO1).isEqualTo(actionHistoryDTO2);
        actionHistoryDTO2.setId(2L);
        assertThat(actionHistoryDTO1).isNotEqualTo(actionHistoryDTO2);
        actionHistoryDTO1.setId(null);
        assertThat(actionHistoryDTO1).isNotEqualTo(actionHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(actionHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(actionHistoryMapper.fromId(null)).isNull();
    }
}
