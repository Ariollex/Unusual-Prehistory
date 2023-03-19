package com.peeko32213.unusualprehistory.datagen;

import com.peeko32213.unusualprehistory.UnusualPrehistory;
import com.peeko32213.unusualprehistory.core.registry.UPBlocks;
import com.peeko32213.unusualprehistory.core.registry.UPItems;
import com.peeko32213.unusualprehistory.core.registry.UPTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagsGenerator extends ItemTagsProvider {
    public ItemTagsGenerator(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, UnusualPrehistory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        /**Example**/
        //tag(UPTags.ALLOWED_FRIDGE_ITEMS).add(UPItems.AMBER_FOSSIL.get());
        tag(UPTags.ALLOWED_FRIDGE_ITEMS)
                .addTag(UPTags.DNA_FLASKS);

        tag(UPTags.FILLED_FLASKS)
                .addTag(UPTags.DNA_FLASKS);

        tag(UPTags.DNA_FLASKS)
                .add(UPItems.AMMONITE_FLASK.get())
                .add(UPItems.QUEREUXIA_FLASK.get())
                .add(UPItems.NELUMBITES_FLASK.get())
                .add(UPItems.CLATHRODICTYON_FLASK.get())
                .add(UPItems.ARCHAEFRUCTUS_FLASK.get())
                .add(UPItems.ANOSTYLOSTRAMA_FLASK.get())
                .add(UPItems.ARCHAO_FLASK.get())
                .add(UPItems.SARR_FLASK.get())
                .add(UPItems.BENNET_FLASK.get())
                .add(UPItems.GINKGO_FLASK.get())
                .add(UPItems.TALL_HORSETAIL_FLASK.get())
                .add(UPItems.HORSETAIL_FLASK.get())
                .add(UPItems.ERYON_FLASK.get())
                .add(UPItems.PACHY_FLASK.get())
                .add(UPItems.TRIKE_FLASK.get())
                .add(UPItems.RAPTOR_FLASK.get())
                .add(UPItems.REX_FLASK.get())
                .add(UPItems.BRACHI_FLASK.get())
                .add(UPItems.SCAU_FLASK.get())
                .add(UPItems.COTY_FLASK.get())
                .add(UPItems.ANURO_FLASK.get())
                .add(UPItems.BEELZ_FLASK.get())
                .add(UPItems.MAJUNGA_FLASK.get())
                .add(UPItems.DUNK_FLASK.get())
                .add(UPItems.STETHA_FLASK.get());

        tag(UPTags.FOSSILS)
                .add(UPItems.AMBER_FOSSIL.get())
                .add(UPItems.PLANT_FOSSIL.get())
                .add(UPItems.MEZO_FOSSIL.get())
                .add(UPItems.PALEO_FOSSIL.get());

        tag(UPTags.ANALYZER_ITEMS_INPUT)
                .addTag(UPTags.FOSSILS);

        tag(UPTags.KENTRO_FOOD)
                .add(UPBlocks.HORSETAIL.get().asItem());

        tag(UPTags.MAJUNGA_FOOD)
                .add(UPItems.RAW_COTY.get());

        tag(UPTags.ORANGE_ULUGH_FOOD)
                .add(UPItems.RAW_COTY.get());
        tag(UPTags.YELLOW_ULUGH_FOOD)
                .add(UPItems.GOLDEN_SCAU.get());
        tag(UPTags.WHITE_ULUGH_FOOD)
                .add(UPItems.REX_TOOTH.get());
        tag(UPTags.BLUE_ULUGH_FOOD)
                .add(UPItems.RAW_SCAU.get());

        tag(UPTags.TRIKE_FOOD)
                .add(UPItems.GINKGO_FRUIT.get());

        tag(UPTags.PACHY_FOOD)
                .add(UPItems.RAW_GINKGO_SEEDS.get());

        tag(UPTags.ORGANIC_OOZE)
                .add(UPItems.ORGANIC_OOZE.get());

        tag(ItemTags.LEAVES)
                .add(UPBlocks.GINKGO_LEAVES.get().asItem());

        tag(ItemTags.PLANKS)
                .add(UPBlocks.GINKGO_PLANKS.get().asItem());

        tag(ItemTags.SAPLINGS)
                .add(UPBlocks.GINKGO_SAPLING.get().asItem());

        tag(ItemTags.SIGNS)
                .add(UPBlocks.GINKGO_SIGN.get().asItem());

        tag(ItemTags.SMALL_FLOWERS)
                .add(UPBlocks.LEEFRUCTUS.get().asItem())
                .add(UPBlocks.SARACENIA.get().asItem())
                .add(UPBlocks.HORSETAIL.get().asItem())
                .add(UPBlocks.BENNETTITALES.get().asItem())
                .add(UPBlocks.ARCHAEOSIGILARIA.get().asItem());

        tag(ItemTags.TALL_FLOWERS)
                .add(UPBlocks.TALL_SARACENIA.get().asItem())
                .add(UPBlocks.TALL_HORSETAIL.get().asItem());

        tag(ItemTags.WOODEN_FENCES)
                .add(UPBlocks.GINKGO_FENCE.get().asItem());

        tag(ItemTags.FISHES)
                .add(UPItems.RAW_STETHA.get())
                .add(UPItems.COOKED_STETHA.get())
                .add(UPItems.RAW_SCAU.get())
                .add(UPItems.COOKED_SCAU.get())
                .add(UPItems.GOLDEN_SCAU.get());

        tag(ItemTags.BUTTONS)
                .add(UPBlocks.AMBER_BUTTON.get().asItem())
                .add(UPBlocks.GINKGO_BUTTON.get().asItem());

        tag(ItemTags.WOODEN_BUTTONS)
                .add(UPBlocks.GINKGO_BUTTON.get().asItem());
    }



    @Override
    public String getName() { return UnusualPrehistory.MODID + " Item Tags";}
}