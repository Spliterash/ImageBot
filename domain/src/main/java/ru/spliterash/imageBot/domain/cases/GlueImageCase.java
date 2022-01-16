package ru.spliterash.imageBot.domain.cases;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.ImageCaseContext;
import ru.spliterash.imageBot.domain.def.ImagePipelineCase;
import ru.spliterash.imageBot.domain.def.annotation.Name;
import ru.spliterash.imageBot.domain.def.params.CaseParams;
import ru.spliterash.imageBot.domain.entities.Direction;
import ru.spliterash.imageBot.domain.entities.ImageData;

import java.util.List;

@RequiredArgsConstructor
@Name(value = "Клей", info = "Автоматическая склейка изображения")
public class GlueImageCase implements ImagePipelineCase<GlueImageCase.Params> {
    private final CaseExecutor executor;
    private final ComicImageCase comicImageCase;
    private final LineImagesCase lineImagesCase;

    @Override
    public void execute(ImageCaseContext context, Params params) throws Exception {
        List<ImageData> images = context.getImages();
        int size = images.size();
        if (size <= 3) {
            executor.execute(
                    lineImagesCase,
                    context,
                    LineImagesCase.Params.builder()
                            .needBorder(params.needBorder)
                            .direction(Direction.VERTICAL)
                            .build()
            );
        } else if (size % 3 == 0) {
            executor.execute(
                    comicImageCase,
                    context,
                    ComicImageCase.Params.builder()
                            .needBorder(params.needBorder)
                            .columns(3)
                            .build()
            );

        } else {
            executor.execute(
                    comicImageCase,
                    context,
                    ComicImageCase.Params.builder()
                            .needBorder(params.needBorder)
                            .columns(2)
                            .build()
            );
        }

    }

    @Getter
    @SuperBuilder
    public static class Params extends CaseParams {
        @Builder.Default
        private final boolean needBorder = false;
    }
}
