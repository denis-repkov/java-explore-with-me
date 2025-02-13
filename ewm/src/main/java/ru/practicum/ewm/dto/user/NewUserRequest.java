package ru.practicum.ewm.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.ewm.model.AllowedSubscriberGroup;

@Data
public class NewUserRequest {

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @NotNull
    @Size(min = 6, max = 254)
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+", message = "Электронная почта некорректна")
    private String email;

    private AllowedSubscriberGroup subscriberGroup;

    public AllowedSubscriberGroup getSubscriberGroup() {
        return (subscriberGroup != null) ? subscriberGroup : AllowedSubscriberGroup.ALL;
    }
}