package com.mrbysco.shush.client.screen.widget;

import com.mrbysco.shush.client.screen.ShushScreen;
import com.mrbysco.shush.client.screen.widget.SoundListWidget.ListEntry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoundListWidget extends ObjectSelectionList<ListEntry> {
	private final ShushScreen parent;
	private final int listWidth;

	public SoundListWidget(ShushScreen parent, int listWidth, int top, int bottom) {
		super(parent.getMinecraft(), listWidth, bottom - top, top, parent.getFontRenderer().lineHeight * 2 + 8);
		this.parent = parent;
		this.listWidth = listWidth;
		this.refreshList(parent.getSelectedSounds());
	}

	@Override
	protected int getScrollbarPosition() {
		return this.listWidth - 6;
	}

	@Override
	public int getRowWidth() {
		return this.listWidth;
	}

	public void refreshList(@Nullable List<ResourceLocation> selected) {
		this.clearEntries();
		boolean skipCheck = selected == null || selected.isEmpty();
		parent.buildSoundList(this::addEntry, location -> new ListEntry(location, this.parent, !skipCheck && selected.contains(location)));
	}

	@Override
	public void setSelected(@Nullable SoundListWidget.ListEntry selected) {
		if (selected == getSelected()) return;
		this.parent.setSelected(getSelected(), selected);
		super.setSelected(selected);
	}

	public class ListEntry extends Entry<ListEntry> {
		private final ResourceLocation soundLocation;
		private final ShushScreen parent;
		private boolean selected;

		ListEntry(ResourceLocation location, ShushScreen parent, boolean selected) {
			this.soundLocation = location;
			this.parent = parent;
			this.selected = selected;
		}

		@Override
		public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
			String structureName = soundLocation.toString();
			Component name = Component.literal(structureName);
			Font font = this.parent.getFontRenderer();
			guiGraphics.drawString(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))),
					(this.parent.width / 2) - (font.width(structureName) / 2) + 3, top + 6, 0xFFFffFFF, false);
			if (this.selected)
				guiGraphics.fill(left, top, left + entryWidth, top + entryHeight, 0x80FFFFFF);
		}

		public ResourceLocation getSoundLocation() {
			return soundLocation;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public Component getNarration() {
			return Component.literal(getSoundLocation().getPath());
		}
	}
}