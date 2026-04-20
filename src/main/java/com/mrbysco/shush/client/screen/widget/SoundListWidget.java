package com.mrbysco.shush.client.screen.widget;

import com.mrbysco.shush.client.screen.ShushScreen;
import com.mrbysco.shush.client.screen.widget.SoundListWidget.ListEntry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
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
	protected int scrollBarX() {
		return this.listWidth - 6;
	}

	@Override
	public int getRowWidth() {
		return this.listWidth;
	}

	public void refreshList(@Nullable List<Identifier> selected) {
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
		private final Identifier soundLocation;
		private final ShushScreen parent;
		private boolean selected;

		ListEntry(Identifier location, ShushScreen parent, boolean selected) {
			this.soundLocation = location;
			this.parent = parent;
			this.selected = selected;
		}

		@Override
		public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
			String structureName = soundLocation.toString();
			Component name = Component.literal(structureName);
			Font font = this.parent.getFontRenderer();
			int left = getContentX();
			int top = getContentY();
			int entryWidth = getWidth();
			int entryHeight = getHeight();
			graphics.text(font, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))),
					(this.parent.width / 2) - (font.width(structureName) / 2) + 3, top + 6, 0xFFFFFFFF, false);
			if (this.selected)
				graphics.fill(left, top, left + entryWidth, top + entryHeight, 0x80FFFFFF);
		}

		public Identifier getSoundLocation() {
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