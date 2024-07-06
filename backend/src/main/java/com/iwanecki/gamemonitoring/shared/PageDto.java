package com.iwanecki.gamemonitoring.shared;

import java.util.List;

public record PageDto<T>(Integer page, int elements, long totalElements, List<T> data) {
}
