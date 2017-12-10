package model;

import lombok.val;
import utils.Utils;

public class LevelBuilder {
    private Character[][] map;
    private int portalsCount = 0;
    private int currentPortalID = 0;

    public LevelBuilder(int height, int width) {
        map = new Character[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                map[i][j] = Space.representation;
    }

    public <T extends GameObject> void add(int height, int width, Class<T> cls) {
        if (Utils.tryParseChar(map[height][width]))
            throw new IllegalArgumentException("Can not replace portal");

        if (cls == Portal.class)
            map[height][width] = Character.forDigit(getPortalID(), 10);
        else
            try {
                map[height][width] = (Character) cls.getField("representation").get(null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
    }

    private int getPortalID() {
        if (portalsCount++ % 2 == 0)
            currentPortalID++;

        if (currentPortalID > 9)
            throw new IllegalStateException("It is impossible to put more portals");
        return currentPortalID;
    }

    public String getLevelRepresentation() {
        val builder = new StringBuilder();
        for (Character[] line : map) {
            for (Character symbol : line)
                builder.append(symbol);

            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
