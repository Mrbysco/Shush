package com.mrbysco.shush.util;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;

public enum SourceType implements StringRepresentable {
	MASTER(SoundSource.MASTER),
	MUSIC(SoundSource.MUSIC),
	RECORDS(SoundSource.RECORDS),
	WEATHER(SoundSource.WEATHER),
	BLOCKS(SoundSource.BLOCKS),
	HOSTILE(SoundSource.HOSTILE),
	NEUTRAL(SoundSource.NEUTRAL),
	PLAYERS(SoundSource.PLAYERS),
	AMBIENT(SoundSource.AMBIENT),
	VOICE(SoundSource.VOICE);

	private final SoundSource source;

	SourceType(SoundSource source) {
		this.source = source;
	}

	public SoundSource getSource() {
		return this.source;
	}

	@Override
	public String toString() {
		return this.source.getName();
	}

	@Override
	public String getSerializedName() {
		return this.source.getName();
	}
}
