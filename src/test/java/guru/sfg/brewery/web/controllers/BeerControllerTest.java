/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
class BeerControllerTest {

   @Autowired
   WebApplicationContext wac;

    MockMvc mockMvc;

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

    @InjectMocks
    BeerController controller;
    List<Beer> beerList;
    UUID uuid;
    Beer beer;

    Page<Beer> beers;
    Page<Beer> pagedResponse;

    @BeforeEach
    void setUp() {
        beerList = new ArrayList<Beer>();
        beerList.add(Beer.builder().build());
        beerList.add(Beer.builder().build());
        pagedResponse = new PageImpl(beerList);

        final String id = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
        uuid = UUID.fromString(id);

        mockMvc = MockMvcBuilders
                //.standaloneSetup(controller)
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Disabled
    @WithMockUser("spring")
    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
        verifyNoInteractions(beerRepository);
    }

    @Disabled
    @Test
    void findBeersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
        verifyNoInteractions(beerRepository);
    }

    //ToDO: Mocking Page
     void processFindFormReturnMany() throws Exception{
        when(beerRepository.findAllByBeerName(anyString(), PageRequest.of(0,
              10,Sort.Direction.DESC,"beerName"))).thenReturn(pagedResponse);
        mockMvc.perform(get("/beers"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/beerList"))
                .andExpect(model().attribute("selections", hasSize(2)));
    }

    @Disabled
    @Test
    void showBeer() throws Exception{

        when(beerRepository.findById(uuid)).thenReturn(Optional.of(Beer.builder().id(uuid).build()));
        mockMvc.perform(get("/beers/"+uuid))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/beerDetails"))
                .andExpect(model().attribute("beer", hasProperty("id", is(uuid))));
    }

    @Disabled
    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/beers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
        verifyNoInteractions(beerRepository);
    }

    @Disabled
    @Test
    void processCreationForm() throws Exception {
        when(beerRepository.save(ArgumentMatchers.any())).thenReturn(Beer.builder().id(uuid).build());
        mockMvc.perform(post("/beers/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/beers/"+ uuid));
        verify(beerRepository).save(ArgumentMatchers.any());
    }

    @Disabled
    @Test
    void initUpdateBeerForm() throws Exception{
        when(beerRepository.findById(uuid)).thenReturn(Optional.of(Beer.builder().id(uuid).build()));
        mockMvc.perform(get("/beers/"+uuid+"/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createOrUpdateBeer"))
                .andExpect(model().attributeExists("beer"));
        verifyNoMoreInteractions(beerRepository);
    }

    @Disabled
    @Test
    void processUpdationForm() throws Exception {
        when(beerRepository.save(ArgumentMatchers.any())).thenReturn(Beer.builder().id(uuid).build());

        mockMvc.perform(post("/beers/"+uuid+"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/beers/"+uuid));

        verify(beerRepository).save(ArgumentMatchers.any());
    }
}