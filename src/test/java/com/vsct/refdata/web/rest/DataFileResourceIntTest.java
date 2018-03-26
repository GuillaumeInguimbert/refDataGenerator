package com.vsct.refdata.web.rest;

import com.vsct.refdata.RefDataGeneratorApp;

import com.vsct.refdata.domain.DataFile;
import com.vsct.refdata.repository.DataFileRepository;
import com.vsct.refdata.service.DataFileService;
import com.vsct.refdata.service.dto.DataFileDTO;
import com.vsct.refdata.service.mapper.DataFileMapper;
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

import com.vsct.refdata.domain.enumeration.Status;
import com.vsct.refdata.domain.enumeration.Type;
/**
 * Test class for the DataFileResource REST controller.
 *
 * @see DataFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RefDataGeneratorApp.class)
public class DataFileResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LOGICAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOGICAL_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.PROCESSING;

    private static final Type DEFAULT_TYPE = Type.REF_DATA;
    private static final Type UPDATED_TYPE = Type.FBC_GROUP;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TAGET_PATH = "AAAAAAAAAA";
    private static final String UPDATED_TAGET_PATH = "BBBBBBBBBB";

    @Autowired
    private DataFileRepository dataFileRepository;

    @Autowired
    private DataFileMapper dataFileMapper;

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataFileMockMvc;

    private DataFile dataFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataFileResource dataFileResource = new DataFileResource(dataFileService);
        this.restDataFileMockMvc = MockMvcBuilders.standaloneSetup(dataFileResource)
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
    public static DataFile createEntity(EntityManager em) {
        DataFile dataFile = new DataFile()
            .date(DEFAULT_DATE)
            .logicalName(DEFAULT_LOGICAL_NAME)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .version(DEFAULT_VERSION)
            .sourcePath(DEFAULT_SOURCE_PATH)
            .tagetPath(DEFAULT_TAGET_PATH);
        return dataFile;
    }

    @Before
    public void initTest() {
        dataFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataFile() throws Exception {
        int databaseSizeBeforeCreate = dataFileRepository.findAll().size();

        // Create the DataFile
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);
        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isCreated());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeCreate + 1);
        DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
        assertThat(testDataFile.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDataFile.getLogicalName()).isEqualTo(DEFAULT_LOGICAL_NAME);
        assertThat(testDataFile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDataFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDataFile.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testDataFile.getSourcePath()).isEqualTo(DEFAULT_SOURCE_PATH);
        assertThat(testDataFile.getTagetPath()).isEqualTo(DEFAULT_TAGET_PATH);
    }

    @Test
    @Transactional
    public void createDataFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataFileRepository.findAll().size();

        // Create the DataFile with an existing ID
        dataFile.setId(1L);
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataFileRepository.findAll().size();
        // set the field null
        dataFile.setDate(null);

        // Create the DataFile, which fails.
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLogicalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataFileRepository.findAll().size();
        // set the field null
        dataFile.setLogicalName(null);

        // Create the DataFile, which fails.
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataFileRepository.findAll().size();
        // set the field null
        dataFile.setStatus(null);

        // Create the DataFile, which fails.
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataFileRepository.findAll().size();
        // set the field null
        dataFile.setType(null);

        // Create the DataFile, which fails.
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataFileRepository.findAll().size();
        // set the field null
        dataFile.setVersion(null);

        // Create the DataFile, which fails.
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        restDataFileMockMvc.perform(post("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isBadRequest());

        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDataFiles() throws Exception {
        // Initialize the database
        dataFileRepository.saveAndFlush(dataFile);

        // Get all the dataFileList
        restDataFileMockMvc.perform(get("/api/data-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].logicalName").value(hasItem(DEFAULT_LOGICAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].sourcePath").value(hasItem(DEFAULT_SOURCE_PATH.toString())))
            .andExpect(jsonPath("$.[*].tagetPath").value(hasItem(DEFAULT_TAGET_PATH.toString())));
    }

    @Test
    @Transactional
    public void getDataFile() throws Exception {
        // Initialize the database
        dataFileRepository.saveAndFlush(dataFile);

        // Get the dataFile
        restDataFileMockMvc.perform(get("/api/data-files/{id}", dataFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataFile.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.logicalName").value(DEFAULT_LOGICAL_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.sourcePath").value(DEFAULT_SOURCE_PATH.toString()))
            .andExpect(jsonPath("$.tagetPath").value(DEFAULT_TAGET_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataFile() throws Exception {
        // Get the dataFile
        restDataFileMockMvc.perform(get("/api/data-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataFile() throws Exception {
        // Initialize the database
        dataFileRepository.saveAndFlush(dataFile);
        int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

        // Update the dataFile
        DataFile updatedDataFile = dataFileRepository.findOne(dataFile.getId());
        // Disconnect from session so that the updates on updatedDataFile are not directly saved in db
        em.detach(updatedDataFile);
        updatedDataFile
            .date(UPDATED_DATE)
            .logicalName(UPDATED_LOGICAL_NAME)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .version(UPDATED_VERSION)
            .sourcePath(UPDATED_SOURCE_PATH)
            .tagetPath(UPDATED_TAGET_PATH);
        DataFileDTO dataFileDTO = dataFileMapper.toDto(updatedDataFile);

        restDataFileMockMvc.perform(put("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isOk());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
        DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
        assertThat(testDataFile.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDataFile.getLogicalName()).isEqualTo(UPDATED_LOGICAL_NAME);
        assertThat(testDataFile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDataFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDataFile.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDataFile.getSourcePath()).isEqualTo(UPDATED_SOURCE_PATH);
        assertThat(testDataFile.getTagetPath()).isEqualTo(UPDATED_TAGET_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingDataFile() throws Exception {
        int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

        // Create the DataFile
        DataFileDTO dataFileDTO = dataFileMapper.toDto(dataFile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDataFileMockMvc.perform(put("/api/data-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFileDTO)))
            .andExpect(status().isCreated());

        // Validate the DataFile in the database
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDataFile() throws Exception {
        // Initialize the database
        dataFileRepository.saveAndFlush(dataFile);
        int databaseSizeBeforeDelete = dataFileRepository.findAll().size();

        // Get the dataFile
        restDataFileMockMvc.perform(delete("/api/data-files/{id}", dataFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataFile> dataFileList = dataFileRepository.findAll();
        assertThat(dataFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFile.class);
        DataFile dataFile1 = new DataFile();
        dataFile1.setId(1L);
        DataFile dataFile2 = new DataFile();
        dataFile2.setId(dataFile1.getId());
        assertThat(dataFile1).isEqualTo(dataFile2);
        dataFile2.setId(2L);
        assertThat(dataFile1).isNotEqualTo(dataFile2);
        dataFile1.setId(null);
        assertThat(dataFile1).isNotEqualTo(dataFile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFileDTO.class);
        DataFileDTO dataFileDTO1 = new DataFileDTO();
        dataFileDTO1.setId(1L);
        DataFileDTO dataFileDTO2 = new DataFileDTO();
        assertThat(dataFileDTO1).isNotEqualTo(dataFileDTO2);
        dataFileDTO2.setId(dataFileDTO1.getId());
        assertThat(dataFileDTO1).isEqualTo(dataFileDTO2);
        dataFileDTO2.setId(2L);
        assertThat(dataFileDTO1).isNotEqualTo(dataFileDTO2);
        dataFileDTO1.setId(null);
        assertThat(dataFileDTO1).isNotEqualTo(dataFileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dataFileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dataFileMapper.fromId(null)).isNull();
    }
}
