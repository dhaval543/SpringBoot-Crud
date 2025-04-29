package backend.api;

import backend.java.api.ApiMapper;
import backend.java.entity.Person;
import backend.java.payload.PersonGetResponsePayload;
import backend.java.payload.PersonPatchRequestPayload;
import backend.java.payload.PersonPostRequestPayload;
import backend.java.usecase.CreatePersonUseCase;
import backend.java.usecase.DeletePersonUseCase;
import backend.java.usecase.ReadPersonUseCase;
import backend.java.usecase.UpdatePersonUseCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import backend.java.api.*;
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    static final Person NIKOLA_TESLA = Person.builder().id("67d25c69a735fc1e8a658a86").firstName("admin").lastName("admin").dateOfBirth(LocalDate.of(2016, 2, 4))
            .build();

    @Autowired
    MockMvc mockMvc;

    @Mock
    CreatePersonUseCase createUseCase;

    @Mock
    ReadPersonUseCase readUseCase;

    @Mock
    UpdatePersonUseCase updateUseCase;

    @Mock
    DeletePersonUseCase deleteUseCase;

    @Mock
    ApiMapper mapper;

    @Test
    void getAllPersons() throws Exception {
        List<Person> personsMock = List.of(
                NIKOLA_TESLA,
                Person.builder().id("67d5b6e3967a284931398a1b").firstName("Nikola").lastName("Tesla").dateOfBirth(LocalDate.of(1856, 7, 10))
                        .build(),
                Person.builder().id("67d5b90c967a284931398a1c").firstName("Carl").lastName("Sagan").dateOfBirth(LocalDate.of(1934, 11, 9))
                        .build());
        when(readUseCase.readAll()).thenReturn(personsMock);
        doAnswer(i -> {
            var p = i.getArgument(0, Person.class);
            var r = new PersonGetResponsePayload();
            r.setId(p.getId());
            r.setFirstName(p.getFirstName());
            r.setLastName(p.getLastName());
            r.setDateOfBirth(p.getDateOfBirth());
            return r;
        }).when(mapper).toPersonGetResponsePayload(any(Person.class));

        MvcResult mvcResult = mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn();

        Approvals.verify(mvcResult.getResponse().getContentAsString());

        verify(readUseCase).readAll();
        verify(mapper, times(3)).toPersonGetResponsePayload(any(Person.class));
        verifyNoMoreInteractions(readUseCase, mapper);
    }

    @Test
    void getPersonById() throws Exception {
        var id = "1";

        when(readUseCase.readById(id))
                .thenReturn(NIKOLA_TESLA);
        doAnswer(i -> {
            var p = i.getArgument(0, Person.class);
            var r = new PersonGetResponsePayload();
            r.setId(p.getId());
            r.setFirstName(p.getFirstName());
            r.setLastName(p.getLastName());
            r.setDateOfBirth(p.getDateOfBirth());
            return r;
        }).when(mapper).toPersonGetResponsePayload(any(Person.class));

        MvcResult mvcResult = mockMvc.perform(get("/persons/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        Approvals.verifyJson(mvcResult.getResponse().getContentAsString());

        verify(readUseCase).readById(id);
        verify(mapper).toPersonGetResponsePayload(any(Person.class));
        verifyNoMoreInteractions(readUseCase, mapper);
    }

    @Test
    void createPerson() throws Exception {
        var personPostRequestPayload = new PersonPostRequestPayload();
        personPostRequestPayload.setFirstName(NIKOLA_TESLA.getFirstName());
        personPostRequestPayload.setLastName(NIKOLA_TESLA.getLastName());
        personPostRequestPayload.setDateOfBirth(NIKOLA_TESLA.getDateOfBirth());

        var person = Person.builder()
                .firstName(NIKOLA_TESLA.getFirstName())
                .lastName(NIKOLA_TESLA.getLastName())
                .dateOfBirth(NIKOLA_TESLA.getDateOfBirth())
                .build();

        doAnswer(i -> person)
                .when(mapper).toPerson(personPostRequestPayload);

        doReturn(NIKOLA_TESLA)
                .when(createUseCase).create(person);

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc
                .perform(post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personPostRequestPayload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/persons/1"));

        verify(mapper).toPerson(personPostRequestPayload);
        verify(createUseCase).create(person);
        verifyNoMoreInteractions(mapper, readUseCase);
    }

    @Test
    void updatePerson() throws Exception {
        var id = "1";

        var personPatchRequestPayload = new PersonPatchRequestPayload();
        personPatchRequestPayload.setFirstName("NIKOLA");
        personPatchRequestPayload.setLastName("TESLA");

        var person = Person.builder()
                .firstName("NIKOLA")
                .lastName("TESLA")
                .build();

        doAnswer(i -> person)
                .when(mapper).toPerson(personPatchRequestPayload);

        doReturn(person.toBuilder()
                .id(id)
                .dateOfBirth(NIKOLA_TESLA.getDateOfBirth())
                .build())
                .when(updateUseCase).update(id, person);

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc
                .perform(patch("/persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personPatchRequestPayload)))
                .andExpect(status().isNoContent());

        verify(mapper).toPerson(personPatchRequestPayload);
        verify(updateUseCase).update(id, person);
        verifyNoMoreInteractions(mapper, updateUseCase);
    }

    @Test
    void deletePerson() throws Exception {
        var id = "1";

        doNothing()
                .when(deleteUseCase).deleteById(id);

        mockMvc.perform(delete("/persons/{id}", id))
                .andExpect(status().isNoContent());
    }

}
