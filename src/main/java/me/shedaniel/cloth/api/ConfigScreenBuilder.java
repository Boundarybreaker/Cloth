package me.shedaniel.cloth.api;

import javafx.util.Pair;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import net.minecraft.client.gui.Screen;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface ConfigScreenBuilder {
    
    String getTitle();
    
    void setTitle(String title);
    
    Screen getParentScreen();
    
    void setParentScreen(Screen parent);
    
    Consumer<SavedConfig> getOnSave();
    
    void setOnSave(Consumer<SavedConfig> onSave);
    
    ClothConfigScreen build();
    
    List<String> getCategories();
    
    CategoryBuilder addCategory(String category);
    
    CategoryBuilder getCategory(String category);
    
    default void addCategories(String... categories) {
        for(String category : categories)
            addCategory(category);
    }
    
    void removeCategory(String category);
    
    default void removeCategories(String... categories) {
        for(String category : categories)
            removeCategory(category);
    }
    
    boolean hasCategory(String category);
    
    void addOption(String category, String key, Object object);
    
    void addOption(String category, ClothConfigScreen.AbstractListEntry entry);
    
    List<Pair<String, Object>> getOptions(String category);
    
    void setDoesConfirmSave(boolean confirmSave);
    
    boolean doesConfirmSave();
    
    void setShouldProcessErrors(boolean processErrors);
    
    boolean shouldProcessErrors();
    
    @Deprecated
    public Map<String, List<Pair<String, Object>>> getDataMap();
    
    public static interface CategoryBuilder {
        List<Pair<String, Object>> getOptions();
        
        CategoryBuilder addOption(ClothConfigScreen.AbstractListEntry entry);
        
        @Deprecated
        CategoryBuilder addOption(String key, Object object);
        
        ConfigScreenBuilder removeFromParent();
        
        ConfigScreenBuilder parent();
        
        String getName();
        
        boolean exists();
    }
    
    public static interface SavedConfig {
        boolean containsCategory(String category);
        
        SavedCategory getCategory(String category);
        
        List<SavedCategory> getCategories();
    }
    
    public static interface SavedCategory {
        boolean exists();
        
        String getName();
        
        List<Pair<String, Object>> getOptionPairs();
        
        List<SavedOption> getOptions();
        
        Optional<SavedOption> getOption(String fieldKey);
    }
    
    public static interface SavedOption {
        String getFieldKey();
        
        Object getValue();
    }
    
}
