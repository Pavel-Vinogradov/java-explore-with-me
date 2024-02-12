package ru.practicum.ewm.user.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> getUser(@RequestParam(defaultValue = "") List<Long> ids,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос списка пользователей, параметры from = {}, size = {}", from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Запрос на сохранение пользователя email = {}", newUserRequest.getEmail());
        return userService.createUsers(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @PositiveOrZero long userId) {
        log.info("Удаление пользователя id = {}", userId);
        userService.deleteUser(userId);
    }
}