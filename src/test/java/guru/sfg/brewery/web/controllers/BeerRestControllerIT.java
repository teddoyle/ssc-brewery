package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.BreweryConstants;
import guru.sfg.brewery.web.model.BeerDto;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest
public class BeerRestControllerIT extends BaseIT{

        @Test
        void findBeers() throws Exception {
            String url = BreweryConstants.BEER_API_PREFIX + BreweryConstants.BEER_API_SUFFIX; /* "/api/v1/beer/" */
            System.out.println(" calling '" + url + "' ");
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        }


        public void saveBeerById(UUID uuid) throws Exception {
            BeerDto beerObject = new BeerDto();

            beerObject.setId(uuid);
            beerObject.setBeerName("IPA REST");
            beerObject.setBeerStyle(BeerStyleEnum.IPA);

            String url = BreweryConstants.BEER_API_PREFIX +BreweryConstants.BEER_API_SUFFIX;
            mockMvc.perform(post(url, beerObject))
                    .andExpect(status().isCreated());
        }
        @Test
        void findBeerById() throws Exception {
            UUID uuid = UUID.randomUUID();
            saveBeerById(uuid);
            String url = BreweryConstants.BEER_API_PREFIX
                    + BreweryConstants.BEER_API_SUFFIX
                    + uuid;
            System.out.println(" calling '" + url + "' ");
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
            // deleteBeerById(uuid);
        }

}
