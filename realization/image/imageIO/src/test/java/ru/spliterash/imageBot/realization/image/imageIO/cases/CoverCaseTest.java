package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.domain.cases.CoverImageUseCase;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

public class CoverCaseTest {
    @Test
    public void testGlueCase() {
        ImageIOCoverImageCase coverCase = new ImageIOCoverImageCase(new ImageIOResizeCase());

        DomainImage quadratic = ImageIOTestUtils.loadQuadratic();

        DomainImage domainImage = coverCase.execute(quadratic, CoverImageUseCase.Input.builder()
                .width(200)
                .height(300)
                .cutImage(false)
                .build()).firstImage();


        ImageIOTestUtils.saveImage(domainImage, "coverTestResult.png");
    }
}
