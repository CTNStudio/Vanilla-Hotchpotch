package ctn.vanilla_hotchpotch.datagen;

import ctn.vanilla_hotchpotch.init.VhItems;
import ctn.vanilla_hotchpotch.init.VhTabs;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

import static ctn.vanilla_hotchpotch.VhMain.VH_ID;


public class I18ZhCn extends LanguageProvider {
	public I18ZhCn(PackOutput output) {
		super(output, VH_ID, "zh_cn");
	}

	@Override
	protected void addTranslations() {
		add(VhTabs.BLOCK, "香草乱炖-方块");
		add(VhItems.SAUCEPAN.get(), "炖锅");
		add("config.jade.plugin_vanilla_hotchpotch.saucepan", "炖锅");
	}

	public <T> void add(Supplier<DataComponentType<T>> dataComponentType, String name) {
		add(dataComponentType.get().toString(), name);
	}

	/** 生物属性翻译 */
	public void add(Holder<Attribute> attributeHolder, String name) {
		add(attributeHolder.value().getDescriptionId(), name);
	}

	/**
	 * 创造模式物品栏名称翻译
	 */
	public <R, T extends R> void add(DeferredHolder<R, T> itemGroup, String name) {
		add("itemGroup." + itemGroup.getId().toString().replace(":", "."), name);
	}

	public void addCurios(String curiosIdName, String name) {
		add("curios.identifier." + curiosIdName, name);
		add("curios.modifiers." + curiosIdName, name + "饰品加成：");
	}
}
