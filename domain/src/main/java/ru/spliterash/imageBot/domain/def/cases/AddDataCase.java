package ru.spliterash.imageBot.domain.def.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.ICase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AddDataCase<D> implements ICase<AddDataCase.Input<D>> {

    @Override
    public final CaseIO execute(CaseIO io, Input<D> params) throws ImageReadError {
        return new CaseIO(Stream.concat(
                        Stream.of(wrap(params.data)),
                        io.getValues().stream()
                )
                .collect(Collectors.toList()));
    }


    protected abstract Data wrap(D data);

    @Getter
    @RequiredArgsConstructor
    public static class Input<T> extends CaseParams {
        private final T data;
    }
}
