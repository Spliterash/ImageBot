package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

public class CoverCaseTest {
    @Test
    public void testGlueCase() {
        CaseExecutor executor = new DefaultCaseExecutor();
        ImageIOCoverImageCase coverCase = new ImageIOCoverImageCase(executor, new ImageIOResizeCase());

        ImageData quadratic = ImageIOTestUtils.loadQuadratic();

        ImageData imageData = coverCase.process(quadratic, CoverImageUseCase.Input.builder()
                .width(200)
                .height(300)
                .cutImage(false)
                .build());


        ImageIOTestUtils.saveImage(imageData, "coverTestResult.png");
    }
}
