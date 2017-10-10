//
// @file   CustomProgressBar.java
// @brief  A progress bar based on CoolProgressBar from the RCP Toolbox
// @author Michael Hucka
// @date   Created on 2013-01-19 <mhucka@caltech.edu>
//
// ----------------------------------------------------------------------------
// The contents of this file originally started out as CoolProgressBar.java
// from the RCP Toolbox (https://sourceforge.net/projects/rcptoolbox/).  The
// following is the author and license indicated in that file:
//
//   Author Code Crofter
//   On behalf Polymorph Systems
//   License: Eclipse Public License
//
// No version of the Eclipse Public License is specified, but at the time the
// original code was retrieved, the only license available was v. 1.0, defined
// at http://www.eclipse.org/legal/epl-v10.html
// ----------------------------------------------------------------------------

package org.sbml.testsuite.ui;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;


/**
 * This is a replacement for SWT's ProgressBar.  It does not use the native
 * progress bar widget, and that is in fact the precise reason we use it in
 * the SBML Test Runner.  SWT's ProgressBar class, at least in SWT version
 * 4.2.1 on Mac OS X 10.8.1, interacts unexpectedly with the asynchronous
 * event queue.  I don't know if the problem is documented as a bug in SWT,
 * but it is demonstrable in our code.  The interaction is such that calling
 * ProgressBar.setSelection(...) inside a Runnable spawned by
 * Display.asyncExec(...) or syncExec(...) causes subsequent Runnables in the
 * exec queue *not* to be called until a user interface event like a mouse
 * movement occurs.  Now, if you're reading this and you're familiar with
 * SWT, you probably think that the problem was our code failed to call
 * something like Display.wake(), or update(), or similar methods, from other
 * threads that were running.  Well, I tried that, and many other things, but
 * none of it mattered.  It finally came down to this.  The run() methods we
 * handed to asyncExec(...) contained other calls to update SWT objects, and
 * those interacted properly with display events in the absence of calling
 * ProgressBar.  The addition of one line -- ProgressBar.setSelection(...)
 * -- to the run() method caused all Runnables in the queue to stop being
 * processed unless the user did something like move the mouse.  It didn't
 * matter where the call to ProgressBar was placed inside the run() method
 * (i.e., whether it was placed before or after other SWT calls), nor whether
 * it was followed by a call to wake(), update(), redraw(), etc. inside the
 * run() method.  The effect was most pronounced when the user moved the
 * mouse around in the Tree widget list.  Yes, this is what's known as the
 * "wiggly mouse problem", and yes, it's *supposed* to be solved by calling a
 * method to wake up the queue processing from a thread that performed an
 * action -- but it didn't matter whether we did that or not.
 *
 * Interestingly, in the source code for SWT's ProgressBar implementation of
 * setSelection(...), there is a comment about an interaction with Mac OS's
 * native progress bar and the use of a workaround to force updates:
 *
 * <pre>
 *	* Feature in Cocoa.  The progress bar does
 *	* not redraw right away when a value is
 *	* changed.  This is not strictly incorrect
 *	* but unexpected.  The fix is to force all
 *	* outstanding redraws to be delivered.
 * </pre>
 *
 * That reinforces my belief there's an interaction with the native progress
 * bar on the Mac.  I suspect what's happening is that their "fix" interacts
 * with the event queue used by Display's asyncExec(...).
 *
 * The progress bar implementation here (which is only slightly modified from
 * the original CoolProgressBar) does not suffer from that problem -- a fact
 * which, incidentally, further bolsters the conclusion that the previous
 * problem lies is due to SWT's ProgressBar.  The use of CoolProgressBar was
 * not driven by the belief that it's unique in avoiding the async event
 * problem; it's just that CoolProgressBar was self-contained, simple, and
 * provided an appearance that resembles native progress bars, at least on
 * Mac OS X and roughly on Windows 7.  The progress bar admittedly does not
 * look identical to the Mac OS X native one, but its appearance is also not
 * objectionable (IMHO), and by adjusting the color of the progress bar to
 * match some the color theme used in our SBML Test Runner interface, we can
 * make it look like this was a deliberate stylistic choice.
 *
 * The following is the documentation for the original CoolProgressBar upon
 * which this code is based:
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 *    <dd>SWT.HORIZONTAL</dd>
 *    <dd>SWT.VERTICAL</dd>
 * </dl>

 * <p> The <code>CoolProgressBar</code> is a progress bar class created to
 * provide a dynamic way to have a really (cool) looking progress
 * bar. <br><br>
 *
 * The <code>CoolProgressBar</code> makes use of a 4
 * <code>org.eclipse.swt.graphics.Image</code> classes.<br> 2 of the images
 * are used for the left/top and right/bottom border. <br> 1 image (tiled)
 * will be used for filled region.<br> 1 image (tiled) will be used for empty
 * region.<br><br>
 *
 * <br><br> Sample on how to use the <code>CoolProgressBar</code> is provided
 * in the samples package.  <br><br>
 *
 * @author Code Crofter <br>
 * On behalf Polymorph Systems
 *
 * @since RCP Toolbox v0.1 <br>
 */
public class CustomProgressBar extends Composite
{
    /** Right border region, or bottom border region (SWT.VERTICAL). Image
     *  used in this region is not tiled and the area that this region
     *  occupies is not part of the fill or empty region. */
    private final Canvas rightBorder;

    /** Right region, or bottom region (SWT.VERTICAL). Image used in this
     *  region is tiled. */
    private final Canvas fillRegion;

    /** Left border region, or top border region (SWT.VERTICAL). Image
     *  used in this region is not tiled and the area that this region
     *  occupies is not part of the fill or empty region. */
    private final Canvas leftBorder;

    /** Empty region. */
    private final Canvas emptyRegion;

    /** Flag indicating that the style is horizontal. If not horizontal
     *  the vertical is assumed and visa versa. */
    private final boolean horizontal;

    /**Flag indicating that the style is indeterminate. */
    private final boolean indeterminate;

    /** The recommended width of the progress bar. */
    private final int recommendedWidth;

    /** The recommended height of the progress bar. */
    private final int recommendedHeight;

    /** Refresh rate for the indeterminate scrolling progress bar, in ms. */
    private final int SCROLL_REFRESH_TIME = 50;

    /** The current progressed pixel position. */
    private AtomicInteger progressedPosition = new AtomicInteger(0);

    /** The current progress percentage 0 -> 1. */
    private AtomicLong progressedPercentage = new AtomicLong(0);

    /** The current progress in terms of steps. */
    private AtomicInteger progressedSteps = new AtomicInteger(0);

    /** The number of steps, when using stepping. Default is 100. */
    private AtomicInteger maxSteps = new AtomicInteger(100);

    /** Number of images for the scrolling effect. */
    private final static int NUM_BGIMAGES = 28;

    /** Internal images used for scrolling effect. */
    private final static Image[] bgImage = new Image[NUM_BGIMAGES];


    /**
     * The constructor for the <code>CustomProgressBar</code> to create
     * progress either in vertical (fill from bottom to top) or
     * horizontal (from left to right) alignment. <br>
     *
     * An additional option is given here to specify the focus images for
     * the thumb.
     *
     * @param parent
     * @param style   <code>SWT.VERTICAL</code> or <code>SWT.HORIZONTAL</code>
     * @param leftmost
     * @param left
     * @param right
     * @param rightBorder
     */
    public CustomProgressBar(Composite parent, int style,
                             Image leftBorderImage, Image filledImage,
                             Image emptyImage, Image indeterminateImage,
                             Image rightBorderImage)
    {
        super(parent, SWT.DOUBLE_BUFFERED);

        horizontal = (SWT.VERTICAL != (style & SWT.VERTICAL));
        indeterminate = (SWT.INDETERMINATE == (style & SWT.INDETERMINATE));

        setLayout(createLayout());
        recommendedWidth = leftBorderImage.getBounds().width;
        recommendedHeight = leftBorderImage.getBounds().height;

        if (indeterminate)
        {
            leftBorder = createBorder(this, leftBorderImage);
            fillRegion = createFillRegion(this, filledImage);
            emptyRegion = createEmptyRegion(this, indeterminateImage);
            rightBorder = createBorder(this, rightBorderImage);
        }
        else
        {
            leftBorder = createBorder(this, leftBorderImage);
            fillRegion = createFillRegion(this, horizontal ? filledImage : emptyImage);
            emptyRegion = createEmptyRegion(this, horizontal ? emptyImage : filledImage);
            rightBorder = createBorder(this, rightBorderImage);
        }

        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e)
            {
                long progress = progressedPercentage.get();
                double percent = Double.longBitsToDouble(progress);
                double val = (horizontal ? percent : 1 - percent);
                progressedPosition.set((int) (calculatePotentialFillExtent() * val));
                fillRegion.setLayoutData(
                     createFilledLayoutData(new Rectangle(0, 0,
                                                          recommendedWidth, 
                                                          recommendedHeight)));
                layout();
            }
        });

        if (indeterminate)
        {
            for (int i = 0; i < NUM_BGIMAGES; i++)
            {
                String f = String.format("filled_indeterminate_%02d.png", i);
                bgImage[i] = UIUtils.getImageResource(f);
            }
            activateScrolling(parent.getDisplay());
        }
    }


    public CustomProgressBar(Composite parent, int style)
    {
        this(parent, style,
             UIUtils.getImageResource("border.png"),
             UIUtils.getImageResource("filled_horizontal.png"),
             UIUtils.getImageResource("empty_region.png"),
             UIUtils.getImageResource("filled_indeterminate_00.png"),
             UIUtils.getImageResource("border.png"));
    }


    private GridLayout createLayout()
    {
        final GridLayout gl = new GridLayout(horizontal ? 4 : 1, false);
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 1;
        return gl;
    }


    private Canvas createBorder(Composite parent, Image image)
    {
        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setBackgroundImage(image);
        final GridData gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
        gd.minimumHeight = recommendedHeight;
        gd.heightHint = recommendedHeight;
        gd.minimumWidth = recommendedWidth;
        gd.widthHint = recommendedWidth;
        canvas.setLayoutData(gd);
        return canvas;
    }


    private Canvas createFillRegion(Composite parent, Image image)
    {
        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setBackgroundImage(image);
        canvas.setLayoutData(createFilledLayoutData(image.getBounds()));
        return canvas;
    }


    private GridData createFilledLayoutData(Rectangle bounds)
    {
        int h_setting = horizontal ? SWT.BEGINNING : SWT.FILL;
        int v_setting = horizontal ? SWT.FILL : SWT.BEGINNING;
        final GridData gd = new GridData(h_setting, v_setting, false, false);
        if (horizontal)
        {
            gd.minimumHeight = bounds.height;
            gd.heightHint = bounds.height;
            gd.widthHint = progressedPosition.get();
            gd.minimumWidth = progressedPosition.get();
        }
        else
        {
            gd.minimumWidth = bounds.width;
            gd.widthHint = bounds.width;
            gd.heightHint = progressedPosition.get();
            gd.minimumHeight = progressedPosition.get();
        }
        return gd;
    }


    private Canvas createEmptyRegion(Composite parent, Image image)
    {
        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setBackgroundImage(image);
        int h_setting = horizontal ? SWT.FILL : SWT.FILL;
        int v_setting = horizontal ? SWT.BEGINNING : SWT.FILL;

        Rectangle bounds = image.getBounds();
        final GridData gd = new GridData(h_setting, v_setting, horizontal, !horizontal);
        if (horizontal)
        {
            gd.minimumHeight = bounds.height;
            gd.heightHint = bounds.height;
        }
        else
        {
            gd.minimumWidth = bounds.width;
            gd.widthHint = bounds.width;
        }
        canvas.setLayoutData(gd);
        return canvas;
    }


    /**
     * Activate indefinite scrolling behavior.
     *
     * The approach to creating a timer below is based on code from
     * http://stackoverflow.com/a/16111306/743730
     */
    private void activateScrolling(final Display display)
    {
        display.timerExec(SCROLL_REFRESH_TIME, new Runnable() {
                private int index = -1;
                public void run()
                {
                    if (emptyRegion == null || emptyRegion.isDisposed()) return;
                    index = ++index % NUM_BGIMAGES;
                    emptyRegion.setBackgroundImage(bgImage[index]);
                    display.timerExec(SCROLL_REFRESH_TIME, this);
                }
            });
    }


    /**
     * Method to update the progress.
     * 
     * @param percentage between 0 and 100%
     */
    public void updateProgress(double percentage)
    {
        checkWidget();
        if (percentage < 0)
            percentage = 0;
        else if (percentage > 1)
            percentage = 1;
        progressedPercentage.set(Double.doubleToLongBits(percentage));
        progressedPosition.set((int)(calculatePotentialFillExtent()
                                     * (horizontal ? percentage : 1 - percentage)));
        Rectangle rect = new Rectangle(0,0,recommendedWidth,recommendedHeight);
        fillRegion.setLayoutData(createFilledLayoutData(rect));
        layout();
    }


    /**
     * Update the progress when using step mode.
     */
    public void updateProgressStep()
    {
        progressedSteps.getAndIncrement();
        updateProgress((double)progressedSteps.get()/(double)maxSteps.get());
    }


    /**
     * Set current steps to a specific value.
     */
    public void updateProgressSteps(int num)
    {
        if (num >= maxSteps.get())
        {
            progressedSteps.set(maxSteps.get());
            updateProgress(1);
        }
        else if (num <= 0)
        {
            progressedSteps.set(0);
            updateProgress(0);
        }
        else
        {
            progressedSteps.set(num);
            updateProgress((double)num/(double)maxSteps.get());
        }
    }


    public void setMaxSteps(int num)
    {
        maxSteps.set(num);
    }


    public void resetSteps()
    {
        progressedSteps.set(0);
        updateProgress(0);
    }


    /** calculate the potential fill extent of the progress bar*/
    private int calculatePotentialFillExtent()
    {
        if (horizontal)
            return getClientArea().width - leftBorder.getBounds().width
                - rightBorder.getBounds().width;
        else
            return getClientArea().height - leftBorder.getBounds().height
                - rightBorder.getBounds().height;
    }


    /** The current progress range 0 -> 1*/
    public double getCurrentProgress()
    {
        checkWidget();
        return Double.longBitsToDouble(progressedPercentage.get()); 
    }


    @Override
    public final void setLayout(Layout layout)
    {
        super.setLayout(layout);
    }


    @Override
    public Point computeSize(int wHint, int hHint)
    {
        checkWidget();
        int width = recommendedWidth;
        int height = recommendedHeight;
        if (wHint != SWT.DEFAULT) width = wHint;
        if (hHint != SWT.DEFAULT) height = hHint;
        return new Point(width, height);
    }
}
