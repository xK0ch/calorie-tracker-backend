package de.fynnkoch.calorietracker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class IntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullWorkflow_createProfileIngredientsAndMeals() throws Exception {
        // 1. Create profile
        MvcResult profileResult = mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "Tom"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tom"))
                .andExpect(jsonPath("$.calorieGoal").value(2000))
                .andReturn();

        Long profileId = objectMapper.readTree(profileResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 2. Create ingredient
        MvcResult ingredientResult = mockMvc.perform(post("/profiles/" + profileId + "/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Reis",
                                "referenceAmount", 100,
                                "calories", 130,
                                "fat", 0.3,
                                "protein", 2.7,
                                "carbs", 28
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Reis"))
                .andReturn();

        Long ingredientId = objectMapper.readTree(ingredientResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 3. List ingredients
        mockMvc.perform(get("/profiles/" + profileId + "/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Reis"));

        // 4. Create meal with ingredient
        MvcResult mealResult = mockMvc.perform(post("/profiles/" + profileId + "/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Reis Portion",
                                "ingredients", List.of(Map.of(
                                        "ingredientId", ingredientId,
                                        "amount", 200
                                ))
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Reis Portion"))
                .andExpect(jsonPath("$.ingredients[0].amount").value(200))
                .andReturn();

        Long mealId = objectMapper.readTree(mealResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 5. Add meal to day
        mockMvc.perform(post("/profiles/" + profileId + "/days/2026-03-11/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("mealId", mealId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mealIds[0]").value(mealId));

        // 6. Get day entry
        mockMvc.perform(get("/profiles/" + profileId + "/days/2026-03-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealIds[0]").value(mealId));

        // 7. Get days in range
        mockMvc.perform(get("/profiles/" + profileId + "/days")
                        .param("from", "2026-03-01")
                        .param("to", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mealIds[0]").value(mealId));

        // 8. Update settings
        mockMvc.perform(put("/profiles/" + profileId + "/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("calorieGoal", 1800))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calorieGoal").value(1800));

        // 9. Remove meal from day
        mockMvc.perform(delete("/profiles/" + profileId + "/days/2026-03-11/meals/0"))
                .andExpect(status().isNoContent());

        // 10. Verify day is empty
        mockMvc.perform(get("/profiles/" + profileId + "/days/2026-03-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealIds").isEmpty());

        // 11. Delete profile (cascade deletes all data)
        mockMvc.perform(delete("/profiles/" + profileId))
                .andExpect(status().isNoContent());
    }
}
