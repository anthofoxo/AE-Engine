package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GuiAlignment {

	LEFT(NVG_ALIGN_LEFT), CENTER(NVG_ALIGN_CENTER), RIGHT(NVG_ALIGN_RIGHT),
	TOP(NVG_ALIGN_TOP), MIDDLE(NVG_ALIGN_MIDDLE), BOTTOM(NVG_ALIGN_BOTTOM);

	int mask;

}