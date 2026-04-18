package com.mrbysco.shush.client.screen;

import com.mrbysco.shush.client.screen.widget.SoundListWidget;
import com.mrbysco.shush.network.message.SetShushPayload;
import com.mrbysco.shush.util.ShushData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.fml.loading.StringUtils;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShushScreen extends Screen {

	private enum SortType {
		NORMAL,
		A_TO_Z,
		Z_TO_A;

		Button button;

		Component getButtonText() {
			return Component.translatable("shush.screen.search." + name().toLowerCase(Locale.ROOT));
		}
	}

	private final GlobalPos globalPos;

	private static final int PADDING = 6;
	private SoundListWidget soundsWidget;
	private SoundListWidget.ListEntry selected = null;
	private int listWidth;
	private List<Identifier> soundIds;
	private final List<Identifier> unsortedSoundIds;
	private Button insertButton;
	private Button removeButton;
	private Button saveButton;

	private final int buttonMargin = 1;
	private final int numButtons = SortType.values().length;
	private String lastFilterText = "";

	private EditBox search;
	private boolean sorted = false;
	private SortType sortType = SortType.NORMAL;

	private ExtendedSlider shushSlider;
	private float shushAmount = 0.0F;
	private final List<Identifier> selectedSounds = new ArrayList<>();

	public ShushScreen(GlobalPos pos, @Nullable ShushData data, List<Identifier> sounds) {
		super(Component.translatable("shush.advanced.screen"));
		this.globalPos = pos;

		List<Identifier> selectedSounds = new ArrayList<>();
		if (data != null) {
			data.filteredSounds().forEach(sound -> selectedSounds.add(sound.location()));
			this.shushAmount = data.shushAmount();
		}
		this.selectedSounds.addAll(selectedSounds);

		List<Identifier> soundList = new ArrayList<>(sounds);
		Collections.sort(soundList);
		this.soundIds = Collections.unmodifiableList(soundList);
		this.unsortedSoundIds = Collections.unmodifiableList(sounds);
	}

	public static void openScreen(GlobalPos pos, @Nullable ShushData originalData) {
		Minecraft.getInstance().setScreen(new ShushScreen(pos, originalData, new ArrayList<>(BuiltInRegistries.SOUND_EVENT.keySet())));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		int centerWidth = this.width / 2;
		for (Identifier structureLocation : soundIds) {
			listWidth = Math.max(listWidth, getFontRenderer().width(structureLocation.toString()) + 10);
		}
		listWidth = Math.max(Math.min(listWidth, width / 3), 200);
		listWidth += listWidth % numButtons != 0 ? (numButtons - listWidth % numButtons) : 0;
		int closeButtonWidth = 196;
		int y = this.height - 20 - PADDING;

		this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> ShushScreen.this.onClose())
				.bounds(centerWidth - (closeButtonWidth / 2) + 4, y, closeButtonWidth / 2, 20).build());

		this.addRenderableWidget(this.saveButton = Button.builder(Component.translatable("shush.screen.selection.save"), b -> {
			if (selected != null) {
				int percentage = shushSlider.getValueInt();
				float shush = percentage / 100.0F;
				ClientPacketDistributor.sendToServer(new SetShushPayload(globalPos, selectedSounds, Mth.clamp(shush, 0.1F, 1.0F)));
			}

			if (this.minecraft.player != null && selected != null)
				this.minecraft.player.sendSystemMessage(Component.translatable("shush.screen.selection.saved").withStyle(ChatFormatting.GOLD));
			this.onClose();
		}).bounds(centerWidth + PADDING + 3, y, closeButtonWidth / 2, 20).build());

		y -= 18 + PADDING;
		this.addRenderableWidget(this.removeButton = Button.builder(Component.translatable("shush.screen.selection.remove"), (button) -> {
			selectedSounds.remove(selected.getSoundLocation());
			selected.setSelected(false);
		}).bounds(centerWidth - (closeButtonWidth / 2) + 4, y, closeButtonWidth / 2, 20).build());

		this.addRenderableWidget(this.insertButton = Button.builder(Component.translatable("shush.screen.selection.select"), (button) -> {
			selectedSounds.add(selected.getSoundLocation());
			selected.setSelected(true);
		}).bounds(centerWidth + PADDING + 3, y, closeButtonWidth / 2, 20).build());

		y -= 12 + PADDING;
		search = new EditBox(getFontRenderer(), centerWidth - listWidth / 2 + PADDING + 1, y, listWidth - 2, 14,
				Component.translatable("shush.screen.search"));
		search.setFocused(false);
		search.setCanLoseFocus(true);
		addWidget(search);

		int fullButtonHeight = PADDING + 20 + PADDING;
		this.soundsWidget = new SoundListWidget(this, width, fullButtonHeight, search.getY() - getFontRenderer().lineHeight - PADDING);
		this.soundsWidget.setX(0);
		addWidget(soundsWidget);

		final int width = listWidth / numButtons;
		int x = this.width - 80 + PADDING - width;
		addRenderableWidget(SortType.A_TO_Z.button = Button.builder(SortType.A_TO_Z.getButtonText(), b ->
						resortSounds(SortType.A_TO_Z))
				.bounds(x, PADDING, width - buttonMargin, 20).build());
		x += width + buttonMargin;
		addRenderableWidget(SortType.Z_TO_A.button = Button.builder(SortType.Z_TO_A.getButtonText(), b ->
						resortSounds(SortType.Z_TO_A))
				.bounds(x, PADDING, width - buttonMargin, 20).build());

		resortSounds(SortType.A_TO_Z);
		shushSlider = new ExtendedSlider(10, PADDING, 100, 20,
				Component.translatable("shush.screen.shush_amount"), Component.empty(),
				10, 100, shushAmount, 10, 2, true);
		addRenderableWidget(shushSlider);
	}

	@Override
	public void tick() {
		soundsWidget.setSelected(selected);

		if (!search.getValue().equals(lastFilterText)) {
			reloadSounds();
			sorted = false;
		}

		if (!sorted) {
			reloadSounds();
			if (sortType == SortType.A_TO_Z) {
				Collections.sort(soundIds);
			} else if (sortType == SortType.Z_TO_A) {
				soundIds.sort(Collections.reverseOrder());
			}
			soundsWidget.refreshList(this.selectedSounds);
			if (selected != null) {
				selected = soundsWidget.children().stream().filter(e -> e == selected).findFirst().orElse(null);
			}
			sorted = true;
		}
	}

	public <T extends ObjectSelectionList.Entry<T>> void buildSoundList(Consumer<T> ListViewConsumer, Function<Identifier, T> newEntry) {
		soundIds.forEach(mod -> ListViewConsumer.accept(newEntry.apply(mod)));
	}

	private void reloadSounds() {
		this.soundIds = this.unsortedSoundIds.stream().
				filter(location -> {
							return StringUtils.toLowerCase(location.toString())
									.contains(StringUtils.toLowerCase(search.getValue()));
						}
				)
				.collect(Collectors.toList());
		this.soundsWidget.setScrollAmount(0);
		lastFilterText = search.getValue();
	}

	private void resortSounds(SortType newSort) {
		this.sortType = newSort;

		for (SortType sort : SortType.values()) {
			if (sort.button != null)
				sort.button.active = sortType != sort;
		}
		sorted = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
		this.soundsWidget.extractRenderState(graphics, mouseX, mouseY, partialTicks);

		Component text = Component.translatable("shush.screen.search");
		graphics.centeredText(getFontRenderer(), text, this.width / 2 + PADDING,
				search.getY() - getFontRenderer().lineHeight - 2, 0xFFFFFF);

		this.search.extractRenderState(graphics, mouseX, mouseY, partialTicks);
	}

	public Font getFontRenderer() {
		return font;
	}

	public void setSelected(SoundListWidget.ListEntry entry) {
		this.selected = entry == this.selected ? null : entry;
	}

	public List<Identifier> getSelectedSounds() {
		return selectedSounds;
	}


	/**
	 * Clear the search field when right-clicked on it
	 */
	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		boolean flag = super.mouseClicked(event, doubleClick);
		if (event.button() == 1 && search.isMouseOver(event.x(), event.y())) {
			search.setValue("");
		}
		return flag;
	}

	@Override
	public void resize(int width, int height) {
		String s = this.search.getValue();
		SortType sort = this.sortType;
		SoundListWidget.ListEntry selected = this.selected;
		this.init(width, height);
		this.search.setValue(s);
		this.selected = selected;
		if (!this.search.getValue().isEmpty())
			reloadSounds();
		if (sort != SortType.NORMAL)
			resortSounds(sort);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(null);
	}
}
