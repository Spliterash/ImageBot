package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.domain.cases.CropImageUseCase;
import ru.spliterash.imageBot.domain.entities.ImageData;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

public class CropCaseTest {
//    @Test
    public void testGlueCase() throws Exception {
        CropImageUseCase coverCase = new ImageIOCropUseCase();

        ImageData quadratic = ImageIOTestUtils.loadQuadratic();

        ImageData imageData = coverCase.process(quadratic, CropImageUseCase.Params.builder()
                .top(0)
                .bottom(0)
                .all(5)
                .build());


        ImageIOTestUtils.saveImage(imageData, "cropTestResult.png");
    }
}
