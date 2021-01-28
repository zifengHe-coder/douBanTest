package com.web.spirder.demo.web.utils;

import com.idaoben.utils.dto_assembler.DtoAssembler;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author X
 */
public class DtoTransformer {

    public static class AsPageHelper<U> {
        private final Class<U> dtoClass;

        public AsPageHelper(Class<U> dtoClass) {
            this.dtoClass = dtoClass;
        }

        public <T> Page<U> apply(Page<T> object) {
            return object.map(source -> DtoAssembler.assemble(source, dtoClass));
        }

        public <T> Page<U> apply(Page<T> object, BiConsumer<T, U> postprocessor) {
            return object.map(source -> {
                U result = DtoAssembler.assemble(source, dtoClass);
                postprocessor.accept(source, result);
                return result;
            });
        }

    }

    public static class AsListHelper<U> {
        private final Class<U> dtoClass;

        public AsListHelper(Class<U> dtoClass) { this.dtoClass = dtoClass; }

        public <T> List<U> apply(Collection<T> collection) {
            return collection.stream().map(source -> DtoAssembler.assemble(source, dtoClass))
                    .collect(Collectors.toList());
        }

        public <T> List<U> apply(Collection<T> collection, BiConsumer<T, U> postprocessor) {
            return collection.stream().map(source -> {
                U result = DtoAssembler.assemble(source, dtoClass);
                postprocessor.accept(source, result);
                return result;
            }).collect(Collectors.toList());

        }
    }

    public static class Helper<U> {
        private final Class<U> dtoClass;

        public Helper(Class<U> dtoClass) {
            this.dtoClass = dtoClass;
        }

        public <T> U apply(T domain) {
            return DtoAssembler.assemble(domain, dtoClass);
        }

        public <T> U apply(T domain, BiConsumer<T, U> postprocessor) {
            U result = DtoAssembler.assemble(domain, dtoClass);
            postprocessor.accept(domain, result);
            return result;
        }
    }

    public static <T, U> Function<T, Optional<U>> as(Class<U> dtoClass) {
        return t -> Optional.ofNullable(t).map(o -> DtoAssembler.assemble(o, dtoClass));
    }

    public static <U> Helper<U> nonNullAs(Class<U> dtoClass) {
        return new Helper<>(dtoClass);
    }

    public static <T, U> AsListHelper<U> asList(Class<U> dtoClass) {
        return new AsListHelper<>(dtoClass);
    }

    public static <U> AsPageHelper<U> asPage(Class<U> dtoClass) {
        return new AsPageHelper<>(dtoClass);
    }
}
