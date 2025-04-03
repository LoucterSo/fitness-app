package io.github.LoucterSo.fitness_app.form.error;

import java.util.Map;

public record ValidationErrorResponse(Map<String, String> errorFields, long timeStamp) {

}
