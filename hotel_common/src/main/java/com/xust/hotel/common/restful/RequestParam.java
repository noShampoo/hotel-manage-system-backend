package com.xust.hotel.common.restful;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author bhj
 */
@Getter
public class RequestParam<T> implements Serializable {

    private static final long serialVersionUID = -8257278519922350206L;

    private T data;
}
