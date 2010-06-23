package com.omgen.generator;

import org.apache.commons.cli.Option;

/**
 *
 */
public enum OptionSetting {
	GENERATOR("g"),
	SIMULATION("s"),
	TEMPLATE("t");

	private final String optionKey;

	OptionSetting(String optionKey) {
		this.optionKey = optionKey;
	}

	public static OptionSetting of(String optionKey) {
		for (OptionSetting optionSetting : OptionSetting.values()) {
			if (optionSetting.optionKey().equals(optionKey)) {
				return optionSetting;
			}
		}
		return null;
	}

	public static OptionSetting of(Option option) {
		for (OptionSetting optionSetting : OptionSetting.values()) {
			if (optionSetting.optionKey().equals(option.getOpt())) {
				return optionSetting;
			}
		}
		return null;
	}

	public String optionKey() {
		return optionKey;
	}
}
