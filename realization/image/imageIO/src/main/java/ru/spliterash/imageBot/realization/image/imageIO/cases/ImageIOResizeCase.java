package ru.spliterash.imageBot.realization.image.imageIO.cases;

import ru.spliterash.imageBot.domain.cases.ResizeCase;
import ru.spliterash.imageBot.domain.def.CaseIO;
import ru.spliterash.imageBot.domain.entities.DomainImage;
import ru.spliterash.imageBot.domain.exceptions.ImageReadError;
import ru.spliterash.imageBot.realization.image.imageIO.entities.ImageIOImage;
import ru.spliterash.imageBot.realization.image.imageIO.utils.ImageIOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageIOResizeCase implements ResizeCase {
    @Override
    public CaseIO execute(CaseIO io, Input params) throws ImageReadError {
        if (params.getProportion() == 1)
            return of(io.firstImage());
        DomainImage domainImage = io.firstImage();

        int newX = (int) Math.floor((double) domainImage.getWidth() * params.getProportion());
        int newY = (int) Math.floor((double) domainImage.getHeight() * params.getProportion());

        int type = BufferedImage.TYPE_INT_ARGB;

        Image image = ImageIOUtils.loadImage(domainImage);

        BufferedImage resizedImage = new BufferedImage(newX, newY, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, newX, newY, null);
        g.dispose();


        return of(new ImageIOImage(resizedImage));
    }

}
