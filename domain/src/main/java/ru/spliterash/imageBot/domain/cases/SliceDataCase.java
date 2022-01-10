package ru.spliterash.imageBot.domain.cases;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.ImageCaseContext;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.cases.markers.NoReadCase;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Data;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.domain.exceptions.CaseErrorException;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Name(
        value = "Обрезка массива",
        info = "Удаляет из входящего массива данных данные"
)
public class SliceDataCase implements ImagePipelineCase<SliceDataCase.Input>, NoReadCase {

    @Override
    public void execute(ImageCaseContext context, Input params) throws ImageReadError {
        List<ImageData> images = context.getImages();

        int operation = params.getOperation();

        try {
            switch (params.type) {
                case SLICE:
                    if (operation > 0)
                        images = images.subList(operation, images.size() - 1);
                    else if (operation < 0)
                        images = images.subList(0, (images.size()) + operation);
                    break;
                case INDEX:
                    images = new ArrayList<>(images);
                    images.remove(params.operation - 1);
                    break;
            }
            context.set(images);
        } catch (IndexOutOfBoundsException ex) {
            throw new CaseErrorException("Выход за пределы массива");
        }
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
