package ru.spliterash.imageBot.realization.image.imageIO.cases;

import org.junit.jupiter.api.Test;
import ru.spliterash.imageBot.domain.cases.GlueImagesCase;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOTestUtils;

import java.util.List;

public class GlueCaseTest {
    @Test
    public void testGlueCase() {
        ImageIOResizeCase resize = new ImageIOResizeCase();
        ImageIoGlueImageCaseImpl caseImpl = new ImageIoGlueImageCaseImpl(resize, new ImageIOCoverImageCase(resize));

        List<DomainImage> cats = ImageIOTestUtils.loadCats();

        DomainImage domainImage = caseImpl.execute(cats, GlueImagesCase.GlueImagesInput.builder()
                .resizeMode(GlueImagesCase.ResizeMode.COVER)
                .columns(3)
                .max(99999)
                .padding(10)
                .needBorder(false)
                .build()).firstImage();


        ImageIOTestUtils.saveImage(domainImage, "result.png");
    }
}
