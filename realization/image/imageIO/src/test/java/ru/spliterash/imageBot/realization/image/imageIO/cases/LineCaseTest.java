package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.cases.LineImagesCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.executors.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.entities.Direction;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.cases.line.ImageIOLineCase;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

import java.util.List;

public class LineCaseTest {
//    @Test
    public void test() {
        CaseExecutor executor = new DefaultCaseExecutor();
        ImageIOResizeCase resize = new ImageIOResizeCase();
        CoverImageUseCase coverImageUseCase = new ImageIOCoverImageCase(executor, resize);
        LineImagesCase caseImpl = new ImageIOLineCase(executor, coverImageUseCase);

        List<ImageData> cats = ImageIOTestUtils.loadCats();

        ImageData imageData = executor.execute(caseImpl, cats, LineImagesCase.Params.builder()
                .direction(Direction.HORIZONTAL)
                .needBorder(true)
                .build());


        ImageIOTestUtils.saveImage(imageData, "lineTestResult.png");
    }
}
