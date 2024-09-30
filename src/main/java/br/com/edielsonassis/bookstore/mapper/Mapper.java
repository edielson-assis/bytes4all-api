package br.com.edielsonassis.bookstore.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;

public class Mapper {
    
    private static ModelMapper mapper = new ModelMapper();

    public static <S, T> T parseObject(S source, Class<T> target) {
        return mapper.map(source, target);
    }  
    
    public static <S, T> List<T> parseListObjects(List<S> source, Class<T> target) {
        return source.stream().map(s -> mapper.map(s, target)).toList();
    }
}