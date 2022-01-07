package ru.spliterash.imageBot.domain.cases;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.def.PipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.markers.NoReadCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Name(
        value = "Обрезка массива",
        info = "Удаляет из входящего массива данных данные"
)
public class SliceDataCase implements PipelineCase<SliceDataCase.Input>, NoReadCase {

    @Override
    public CaseIO execute(CaseIO io, Input params) throws ImageReadError {
        CaseIO.Container<? extends Data> container = io.get(params.dataType);

        List<? extends Data> needData = container.getNeedData();
        List<? extends Data> anotherData = container.getRestData();

        int operation = params.getOperation();

        try {
            switch (params.type) {
                case SLICE:
                    if (operation > 0)
                        needData = needData.subList(operation, needData.size() - 1);
                    else if (operation < 0)
                        needData = needData.subList(0, (needData.size()) + operation);
                    break;
                case INDEX:
                    needData.remove(params.operation - 1);
            }

        } catch (IndexOutOfBoundsException ex) {
            throw new CaseErrorException("Выход за пределы массива");
        }

        return new CaseIO(
                Stream.concat(needData.stream(), anotherData.stream()).collect(Collectors.toList())
        );
    }

    @Getter
    @SuperBuilder
    @RequiredArgsConstructor
    public static class Input extends CaseParams {
        @NotNull
        @Builder.Default
        @Name("тип с которым проводится операция")
        private final Class<? extends Data> dataType = ImageData.class;
        @Builder.Default
        @NotNull
        @Name("тип операции")
        private final Type type = Type.SLICE;
        private final int operation;

        public enum Type {
            SLICE, INDEX
        }
    }
}
