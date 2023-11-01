package fr.firstmegagame4.multisignals;

import fr.firstmegagame4.multisignals.init.MultiSignalsBlocks;
import fr.firstmegagame4.multisignals.init.MultiSignalsItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiSignals implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("multisignals");

	@Override
	public void onInitialize() {
		MultiSignals.LOGGER.info("MultiSignals Initialization...");

		MultiSignalsBlocks.register();
		MultiSignalsItems.register();
	}

	public static String id() {
		return "multisignals";
	}

	public static Identifier createId(String path) {
		return new Identifier(MultiSignals.id(), path);
	}
}
