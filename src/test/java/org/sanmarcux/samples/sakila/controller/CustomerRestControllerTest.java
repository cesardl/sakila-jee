package org.sanmarcux.samples.sakila.controller;

import org.junit.jupiter.api.Test;
import org.sanmarcux.samples.sakila.AbstractIntegrationTest;
import org.sanmarcux.samples.sakila.SakilaApplication;
import org.sanmarcux.samples.sakila.dto.AddressDTO;
import org.sanmarcux.samples.sakila.dto.CustomerDTO;
import org.sanmarcux.samples.sakila.dto.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created on 26/08/2026.
 *
 * @author Cesardl
 */
@SpringBootTest(classes = SakilaApplication.class)
@AutoConfigureMockMvc
@WithMockUser // these assert resource behaviour, not authentication; see AuthRestControllerTest
public class CustomerRestControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper mapper;

    @Test
    public void readCustomer() throws Exception {
        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.firstName", is("MARY")))
                .andExpect(jsonPath("$._links.self.href", notNullValue()));
    }

    @Test
    public void customerNotFound() throws Exception {
        mockMvc.perform(get("/customers/99999")).andExpect(status().isNotFound());
    }

    /**
     * save() used to discard the payload and hardcode store 2 / address 591, so every
     * customer landed in the same store no matter what the client asked for. It also never
     * set active, and the primitive field wrote 0, so every customer was born inactive.
     */
    @Test
    public void createCustomer() throws Exception {
        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildCustomer())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.store.storeId", is(1)))
                .andExpect(jsonPath("$.address.addressId", is(1)))
                .andExpect(jsonPath("$.active", is(true)));
    }

    /**
     * An explicit flag in the payload wins over the column default.
     */
    @Test
    public void createInactiveCustomer() throws Exception {
        CustomerDTO customer = buildCustomer();
        customer.setActive(false);

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.active", is(false)));
    }

    /**
     * A store id that does not exist is a 404, not a late foreign key violation.
     */
    @Test
    public void createCustomerWithUnknownStore() throws Exception {
        CustomerDTO customer = buildCustomer();
        customer.getStore().setStoreId(9999);

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isNotFound());
    }

    /**
     * CustomerDTO.store is @NotNull, so @Valid on newCustomer must reject this before the
     * business layer ever sees it. Same for a null address, or either nested id.
     */
    @Test
    public void createCustomerWithMissingStore() throws Exception {
        CustomerDTO customer = buildCustomer();
        customer.setStore(null);

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCustomerWithMissingAddressId() throws Exception {
        CustomerDTO customer = buildCustomer();
        customer.setAddress(new AddressDTO());

        this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest());
    }

    /**
     * A PUT that replaces an existing customer answers 200. It used to answer 201, which
     * is for creation only.
     * <p>
     * Creates and deletes its own row rather than rewriting fixture customer 1, which
     * readCustomer asserts is still MARY -- that only held because JUnit happened to order
     * the two methods favourably, and a rename would have silently flipped it.
     */
    @Test
    public void replaceCustomerReturns200() throws Exception {
        String location = this.mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildCustomer())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        CustomerDTO customer = buildCustomer();
        customer.setLastName("REPLACED");

        this.mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("REPLACED")));

        this.mockMvc.perform(delete(location)).andExpect(status().isNoContent());
    }

    private CustomerDTO buildCustomer() {
        StoreDTO store = new StoreDTO();
        store.setStoreId(1);

        AddressDTO address = new AddressDTO();
        address.setAddressId(1);

        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("INTEGRATION");
        customer.setLastName("TEST");
        customer.setEmail("integration.test@sakilacustomer.org");
        customer.setStore(store);
        customer.setAddress(address);
        return customer;
    }
}
