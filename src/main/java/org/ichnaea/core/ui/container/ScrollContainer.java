package org.ichnaea.core.ui.container;

import org.ichnaea.core.ui.semantic.SemanticColor;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class ScrollContainer extends JScrollPane {
    private final Animator animator;
    private boolean animateHinText = true;
    private float animateLocation;
    private boolean show;
    private boolean mouseOver = false;
    private String labelText = "Label";
    private Color lineColor = SemanticColor.PRIMARY;

    private boolean error = false;

    public ScrollContainer() {
        setOpaque(false);
        setVerticalScrollBar(new org.ichnaea.core.ui.container.ScrollBar());
        org.ichnaea.core.ui.container.ScrollBar scroll = new org.ichnaea.core.ui.container.ScrollBar();
        scroll.setOrientation(JScrollBar.HORIZONTAL);
        setVerticalScrollBar(scroll);
        setBorder(new EmptyBorder(20, 3, 3, 3));
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                setAnimateLocation(fraction);
                repaint();
            }
        };

        animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        int width = getWidth();
        int height = getHeight();

        if (mouseOver) {
            g2.setColor(lineColor);
        } else if (error) {
            g2.setColor(SemanticColor.DANGER);
        } else {
            g2.setColor(SemanticColor.SECONDARY);
        }

        g2.fillRect(2, height - 1, width - 4, 1);
        createHintText(g2);
        createLineStyle(g2);
        g2.dispose();
    }

    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(SemanticColor.SECONDARY);
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(labelText, g2);
        double height = in.top;
        double textY = (height - r2.getHeight()) / 2;
        double size;
        if (animateHinText) {
            if (show) {
                size = 18 * (1 - animateLocation);
            } else {
                size = 18 * animateLocation;
            }
        } else {
            size = 18;
        }
        g2.drawString(labelText, in.right, (int) (in.top + textY + ft.getAscent() - size));
    }

    private void createLineStyle(Graphics2D g2) {
        double width = getWidth() - 4;
        int height = getHeight();
        g2.setColor(lineColor);
        double size;
        if (show) {
            size = width * (1 - animateLocation);
        } else {
            size = width * animateLocation;
        }
        double x = (width - size) / 2;
        g2.fillRect((int) (x + 2), height - 2, (int) size, 2);
    }

    public Animator getAnimator() {
        return animator;
    }

    public float getAnimateLocation() {
        return animateLocation;
    }

    public void setAnimateLocation(float animateLocation) {
        this.animateLocation = animateLocation;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
        repaint();
    }

    public boolean isAnimateHinText() {
        return animateHinText;
    }

    public void setAnimateHinText(boolean animateHinText) {
        this.animateHinText = animateHinText;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setError(boolean error) {
        this.error = error;
        if (error) {
            lineColor = SemanticColor.DANGER;
        } else {
            lineColor = SemanticColor.PRIMARY;
        }
        repaint();
    }
}
