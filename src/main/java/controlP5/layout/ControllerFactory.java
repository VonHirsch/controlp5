package controlP5.layout;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PImage;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.*;

import static controlP5.ControlP5Constants.MULTIPLES;

public class ControllerFactory {
    private final PApplet applet;
    private final ControlP5 cp5;
    private final Map<String, Class<? extends ControllerInterface<?>>> controlMap;


    public ControllerFactory(PApplet applet, ControlP5 cp5) {
        this.applet = applet;
        this.cp5 = cp5;


        controlMap = new HashMap<>();
        controlMap.put("Accordion", Accordion.class);
        controlMap.put("Background", Background.class);
//        controlMap.put("Canvas", Canvas.class);
//        controlMap.put("ChartData", ChartData.class);
//        controlMap.put("ChartDataSet", ChartDataSet.class);
        controlMap.put("CheckBox", CheckBox.class);
        controlMap.put("ColorPicker", ColorPicker.class);
        controlMap.put("Group", Group.class);
//        controlMap.put("Label", Label.class);
        controlMap.put("RadioButton", RadioButton.class);
        controlMap.put("Textarea", Textarea.class);
//        controlMap.put("TickMark", TickMark.class);
//        controlMap.put("Tooltip", Tooltip.class);

        controlMap.put("Accordion", Accordion.class);
        controlMap.put("Bang", Bang.class);
        controlMap.put("Button", Button.class);
        controlMap.put("ButtonBar", ButtonBar.class);
        controlMap.put("Chart", Chart.class);
        controlMap.put("ColorWheel", ColorWheel.class);
        controlMap.put("Icon", Icon.class);
        controlMap.put("Knob", Knob.class);
        controlMap.put("ListBox", ListBox.class);
        controlMap.put("Matrix", Matrix.class);
        controlMap.put("MultiList", MultiList.class);
        controlMap.put("MultilineTextfield", MultilineTextfield.class);
        controlMap.put("Numberbox", Numberbox.class);
        controlMap.put("Range", Range.class);
        controlMap.put("ScrollableList", ScrollableList.class);
        controlMap.put("Slider", Slider.class);
        controlMap.put("Slider2D", Slider2D.class);
        controlMap.put("Textfield", Textfield.class);
        controlMap.put("Textlabel", Textlabel.class);
        controlMap.put("Toggle", Toggle.class);
        controlMap.put("Spacer",Spacer.class);
    }

    /* creates  a ControllerInterface based on the controllerTypeName */
    public ControllerInterface<?> createController(String controllerTypeName, String name) {
        /* Class of the desired controller */
        Class<? extends ControllerInterface<?>> controllerClass = controlMap.get(controllerTypeName);

        if (controllerClass == null) {
            throw new IllegalArgumentException("Invalid control name: " + controllerTypeName);
        }

        try {
            //instantiate the controller
            Constructor<? extends ControllerInterface<?>> constructor = controllerClass.getConstructor(ControlP5.class, String.class);
            if(name == null){
                name = UUID.randomUUID().toString();
            }
            ControllerInterface<?> controller = constructor.newInstance(cp5, name);

            return controller;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create control: " + controllerTypeName, e);
        }
    }


    public void configure(ControllerInterface<?> controller, LinkedHashMap<String, LayoutBuilder.Attribute<?>> attributes, Group parent) {

        //default confs
        if (controller instanceof Controller && !(controller instanceof Button)) {
            ((Controller) controller).getCaptionLabel().hide();

        } else if (controller instanceof Group) {
            ((Group) controller).hideBar();
        }


        for (Map.Entry<String, LayoutBuilder.Attribute<?>> entry : attributes.entrySet()) {
            String attrName = entry.getKey();
            LayoutBuilder.Attribute<?> attribute = entry.getValue();

            int width = 0;
            int height = 0;
            switch (attrName) {

                case "x":
                    int x = (int) attribute.getValue();
                    controller.setPosition(x, controller.getPosition()[1]);
                    break;
                case "y":
                    int y = (int) attribute.getValue();
                    controller.setPosition(controller.getPosition()[0], y);
                    break;
                case "width":
                    //if attribute has an int inside
                    if (attribute.getValue() instanceof Integer) {
                        width = (int) attribute.getValue();
                        height = controller.getHeight();
                        if (controller instanceof Group) {
                            height = ((Group) controller).getBackgroundHeight();
                        }
                        controller.setSize(width, height);
                    }//if it has a percentage
                    else if (attribute.getValue() instanceof LayoutBuilder.Percentage) {
                        LayoutBuilder.Percentage percentage = (LayoutBuilder.Percentage) attribute.getValue();
                        float percentageValue = percentage.percentage;
                        width = Math.round (percentageValue / 100.0f * parent.getWidth());
                        height = 0;
                        if (controller instanceof Group) {
                            height = ((Group) controller).getBackgroundHeight();
                        } else {
                            height = controller.getHeight();
                        }
                        controller.setSize(width, height);
                    }
                    break;
                case "height":
                    //if attribute has an int inside
                    if (attribute.getValue() instanceof Integer) {
                        height = (int) attribute.getValue();
                        width = controller.getWidth();
                        controller.setSize(width, height);
                    }
                    //if it has a percentage
                    else if (attribute.getValue() instanceof LayoutBuilder.Percentage) {
                        float percentageValue =  ((LayoutBuilder.Percentage) attribute.getValue()).percentage;
                        int parentHeight = parent.getBackgroundHeight();
                        height = Math.round((percentageValue) / 100.0f * parentHeight);
                        width = controller.getWidth();
                        controller.setSize(width, height);
                    }

                    break;
//                case "position":
//                    break;
//                case "size":
//                    break;
                case "background":
//                     if attribute has a color inside
                    if (attribute.getValue() instanceof Color) {
                        Color color = (Color) attribute.getValue();
                        int a = color.getAlpha();
                        int r = color.getRed();
                        int g = color.getGreen();
                        int b = color.getBlue();
                        int colorInt = (a << 24) | (r << 16) | (g << 8) | b;
                        if (controller instanceof Group) {
                            ((Group) controller).setBackgroundColor(colorInt);
                        } else {
                            controller.setColorBackground(colorInt);
                        }
                    }
                    break;
                case "text":
                    String text = (String) attribute.getValue();
                    if (controller instanceof Textlabel) {
                        ((Textlabel) controller).setText(text);
                    } else if (controller instanceof Textfield) {
                        ((Textfield) controller).setText(text);
                    } else if (controller instanceof Button) {
                        ((Button) controller).getCaptionLabel().setText(text);
                    }
                    break;
                case "fontSize":
                    Label label = null;
                    int size = (int) attribute.getValue();
                    if(controller instanceof Controller){
                        if(controller instanceof Textfield)
                        {
                        label = ((Controller<?>) controller).getValueLabel();
                        }else if(controller instanceof Textlabel){
                            label = ((Textlabel) controller).getValueLabel();
                        }else {
                            label = ((Controller<?>) controller).getCaptionLabel();
                        }
                    }
                    if(label != null){
                        label.setSize(size);
                    }
                    break;
//            case "label":
//                controller.setLabel(attrValue);
//                break;
//            case "visible":
//                controller.setVisible(Boolean.parseBoolean(attrValue));
//                break;
//            case "captionLabel":
//                controller.getCaptionLabel().setText(attrValue);
//                break;
//            case "valueLabel":
//                controller.getValueLabel().setText(attrValue);
//                break;
                case "hideBar":
                    if (controller instanceof Group) {
                        ((Group) controller).hideBar();
                    }
                    break;
                case "orientation":
                    if (controller instanceof Group) {

                        String orientationString = (String) attribute.getValue();
                        int orientation = 0;
                        if (orientationString.equals("horizontal")) {
                            orientation = 0;
                        } else if (orientationString.equals("vertical")) {
                            orientation = 1;
                        }

                        ((Group) controller).setOrientation(orientation);
                    } else {
                        throw new RuntimeException("Orientation can only be set on a Group. " + controller);
                    }
                    break;
                case "position":
                    //auto positioning system
                    String attrValue = (String) attribute.getValue();
                    if (attrValue.equals("auto")) {
                        //
                    }
                    break;
                case "grid":
                    if (!(controller instanceof Matrix)) {
                        throw new RuntimeException("Grid can only be set on a Matrix. " + controller);
                    }
                    int[] vector = (int[]) attribute.getValue();
                    ((Matrix) controller).setGrid(vector[0], vector[1]);
                    ((Matrix) controller).setMode(MULTIPLES);

                    break;
                case "name":

                    break;
                case "padding":

                    break;
                case "icon":
                    if (!(controller instanceof Button))
                        throw new RuntimeException("Icon can only be set on a Button. " + controller);
                    String iconName = (String) attribute.getValue();
                    PImage normal = cp5.papplet.loadImage(String.format("src/main/resources/icons/%s-normal.png", iconName));
                    PImage hover = cp5.papplet.loadImage(String.format("src/main/resources/icons/%s-hover.png", iconName));
                    PImage active = cp5.papplet.loadImage(String.format("src/main/resources/icons/%s-active.png", iconName));

                    ((Button) controller).setImages(normal, hover, active);


                    break;
                default:
                    System.out.println("Unknown attribute: " + attrName);
            }


        }


        //auto positioning system
        // updates internal state to keep track of the available space for new controllers
        int orientation = parent.getOrientation();
        float[] position = controller.getPosition();
        int[] usedSpace = parent.getUsedSpace();
//
        if (orientation == 0) {  // Horizontal
            controller.setPosition(usedSpace[0], position[1]);
            parent.addUsedSpace(controller.getWidth(), 0);

        } else if (orientation == 1) {  // Vertical
            controller.setPosition(position[0], usedSpace[1]);
            int height;
            if (controller instanceof Group) {
                height = ((Group) controller).getBackgroundHeight();
            } else {
                height = controller.getHeight();
            }
            parent.addUsedSpace(0, height);
        }

        controller.updateAbsolutePosition();
        controller.moveTo(parent);

        if (attributes.containsKey("padding")) {

            //i padding has to run after the layout positioning system
            LayoutBuilder.Attribute attribute = attributes.get("padding");

            float[] p = controller.getPosition();

            int[] padding = new int[4];
            if (attribute.getValue() instanceof Integer) {
                //set all indexes to the same value
                int value = (int) attribute.getValue();
                for (int i = 0; i < padding.length; i++) {
                    padding[i] = value;
                }
            }//else if its an array of int
            else if (attribute.getValue() instanceof int[]) {
                int[] array = (int[]) attribute.getValue();
                for (int i = 0; i < padding.length; i++) {
                    padding[i] = array[i];
                }
            }
            //0 - top
            //1 - right
            //2 - bottom
            //3 - left

            controller.setPosition(p[0] + padding[3], p[1] + padding[0]);
            if (controller instanceof Group) {
                ((Group) controller).setWidth(controller.getWidth() - (padding[1] + padding[3]));
                ((Group) controller).setBackgroundHeight(((Group) controller).getBackgroundHeight() - (padding[0] + padding[2]));
            } else if (controller instanceof Controller) {
                ((Controller) controller).setWidth(controller.getWidth() - (padding[1] + padding[3]));
                ((Controller) controller).setHeight(controller.getHeight() - (padding[0] + padding[2]));
            }
        }

        if (controller instanceof Group) {
            ((Group) controller).didSetupLayout();
        }
    }




    public void addCustomClasses(String key, Class<? extends ControllerInterface<?>> theClass) {
        controlMap.put(key, theClass);
    }
}
