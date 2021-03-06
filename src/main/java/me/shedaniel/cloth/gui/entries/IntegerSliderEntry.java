package me.shedaniel.cloth.gui.entries;

import com.google.common.collect.Lists;
import me.shedaniel.cloth.api.ClientUtils;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.ClothConfigScreen.ListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.InputListener;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntegerSliderEntry extends ListEntry {
    
    protected Slider sliderWidget;
    protected ButtonWidget resetButton;
    protected AtomicInteger value;
    private int minimum, maximum;
    private Consumer<Integer> saveConsumer;
    private Supplier<Integer> defaultValue;
    private Function<Integer, String> textGetter = integer -> String.format("Value: %d", integer);
    private List<InputListener> widgets;
    
    public IntegerSliderEntry(String fieldName, int minimum, int maximum, int value, Consumer<Integer> saveConsumer) {
        this(fieldName, minimum, maximum, value, "text.cloth.reset_value", null, saveConsumer);
    }
    
    public IntegerSliderEntry(String fieldName, int minimum, int maximum, int value, String resetButtonKey, Supplier<Integer> defaultValue, Consumer<Integer> saveConsumer) {
        super(fieldName);
        this.defaultValue = defaultValue;
        this.value = new AtomicInteger(value);
        this.saveConsumer = saveConsumer;
        this.maximum = maximum;
        this.minimum = minimum;
        this.sliderWidget = new Slider(0, 0, 152, 20, ((double) this.value.get() - minimum) / Math.abs(maximum - minimum));
        this.resetButton = new ButtonWidget(0, 0, MinecraftClient.getInstance().textRenderer.getStringWidth(I18n.translate(resetButtonKey)) + 6, 20, I18n.translate(resetButtonKey), widget -> {
            sliderWidget.setProgress((MathHelper.clamp(this.defaultValue.get(), minimum, maximum) - minimum) / (double) Math.abs(maximum - minimum));
            sliderWidget.onProgressChanged();
            sliderWidget.updateText();
        });
        this.sliderWidget.setMessage(textGetter.apply(IntegerSliderEntry.this.value.get()));
        this.widgets = Lists.newArrayList(sliderWidget, resetButton);
    }
    
    public Function<Integer, String> getTextGetter() {
        return textGetter;
    }
    
    public IntegerSliderEntry setTextGetter(Function<Integer, String> textGetter) {
        this.textGetter = textGetter;
        return this;
    }
    
    @Override
    public Integer getObject() {
        return value.get();
    }
    
    @Override
    public Optional<Object> getDefaultValue() {
        return defaultValue == null ? Optional.empty() : Optional.ofNullable(defaultValue.get());
    }
    
    @Override
    public List<? extends InputListener> getInputListeners() {
        return widgets;
    }
    
    public IntegerSliderEntry setMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }
    
    public IntegerSliderEntry setMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }
    
    @Override
    public boolean isActive() {
        return sliderWidget.isHovered() || resetButton.isHovered();
    }
    
    @Override
    public void setActive(boolean b) {
    
    }
    
    @Override
    public InputListener getFocused() {
        return null;
    }
    
    @Override
    public void setFocused(InputListener inputListener) {
    
    }
    
    @Override
    public void onFocusChanged(boolean boolean_1) {
        sliderWidget.onFocusChanged(boolean_1);
    }
    
    @Override
    public boolean isPartOfFocusCycle() {
        return sliderWidget.isPartOfFocusCycle();
    }
    
    @Override
    public void draw(int i, int i1, int i2, int i3, boolean b, float v) {
        Window window = MinecraftClient.getInstance().window;
        Point mouse = ClientUtils.getMouseLocation();
        this.resetButton.active = getDefaultValue().isPresent() && defaultValue.get().intValue() != value.get();
        this.resetButton.y = getY();
        this.sliderWidget.y = getY();
        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(I18n.translate(getFieldName()), window.getScaledWidth() - getX() - MinecraftClient.getInstance().textRenderer.getStringWidth(I18n.translate(getFieldName())), getY() + 5, 16777215);
            this.resetButton.x = getX();
            this.sliderWidget.x = getX() + resetButton.getWidth() + 1;
            this.sliderWidget.setWidth(150 - resetButton.getWidth() - 2);
        } else {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(I18n.translate(getFieldName()), getX(), getY() + 5, 16777215);
            this.resetButton.x = window.getScaledWidth() - getX() - resetButton.getWidth();
            this.sliderWidget.x = window.getScaledWidth() - getX() - 150;
            this.sliderWidget.setWidth(150 - resetButton.getWidth() - 2);
        }
        resetButton.render(mouse.x, mouse.y, v);
        sliderWidget.render(mouse.x, mouse.y, v);
    }
    
    @Override
    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        if (sliderWidget.mouseClicked(double_1, double_2, int_1))
            return true;
        if (resetButton.mouseClicked(double_1, double_2, int_1))
            return true;
        return false;
    }
    
    @Override
    public boolean mouseDragged(double double_1, double double_2, int int_1, double double_3, double double_4) {
        if (sliderWidget.mouseDragged(double_1, double_2, int_1, double_3, double_4))
            return true;
        return false;
    }
    
    private class Slider extends SliderWidget {
        protected Slider(int int_1, int int_2, int int_3, int int_4, double double_1) {
            super(int_1, int_2, int_3, int_4, double_1);
        }
        
        @Override
        public void updateText() {
            setMessage(textGetter.apply(IntegerSliderEntry.this.value.get()));
        }
        
        @Override
        protected void onProgressChanged() {
            IntegerSliderEntry.this.value.set((int) (minimum + Math.abs(maximum - minimum) * progress));
            ((ClothConfigScreen.ListWidget) getParent()).getScreen().setEdited(true);
        }
        
        public double getProgress() {
            return progress;
        }
        
        public void setProgress(double integer) {
            this.progress = integer;
        }
    }
}
