package ru.spliterash.imageBot.realization.image.utils.obj;

import lombok.Value;

@Value
public class Coords {
    int x;
    int y;


    public Coords add(int x, int y) {
        return new Coords(
                this.x + x,
                this.y + y
        );
    }

    public RectangleCoords toRectangle(int radius) {
        return new RectangleCoords(
                new Coords(x + radius, y + radius),
                new Coords(x - radius, y - radius)
        );
    }
}
