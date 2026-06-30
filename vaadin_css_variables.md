# Vaadin & Lumo CSS Custom Properties Reference

Vaadin applications use CSS custom properties (variables) to maintain consistency. Depending on whether you are styling a completely custom theme starting from the **Base Styles** or using the **Lumo** or **Aura** themes, you will target different variable prefixes:
* **`--vaadin-`** for Base Theme custom properties.
* **`--lumo-`** for Lumo Theme custom properties.
* **`--aura-`** for Aura Theme custom properties.

---

## Part 1: Vaadin Base Theme CSS Variables
These define the default, un-themed functional appearance of Vaadin components and support light-dark schemes and Windows High Contrast Mode.

### 1. Colors & Backgrounds
| CSS Custom Property | Default / Purpose |
| :--- | :--- |
| `--vaadin-text-color` | Main text color used in components. |
| `--vaadin-text-color-secondary` | Secondary text color (at least 4.5:1 contrast). |
| `--vaadin-text-color-disabled` | Text color for disabled text. |
| `--vaadin-border-color` | Prominent border color (at least 3:1 contrast). |
| `--vaadin-border-color-secondary`| Less contrast border color for non-essential borders. |
| `--vaadin-background-color` | Base background color for content areas. |
| `--vaadin-background-container` | Background for containers, buttons, toolbars, notifications. |
| `--vaadin-background-container-strong`| A more prominent variant of the container background. |

### 2. Gaps & Paddings
| CSS Custom Property | Default / Purpose |
| :--- | :--- |
| `--vaadin-gap-xs` | Extra-small spacing (e.g., between toolbar buttons). |
| `--vaadin-gap-s` | Small spacing (e.g., between icon and label). |
| `--vaadin-gap-m` | Medium spacing (e.g., padding within a toolbar). |
| `--vaadin-gap-l` | Large spacing (e.g., between form fields). |
| `--vaadin-gap-xl` | Extra-large spacing. |
| `--vaadin-padding-xs` / `-s` / `-m` / `-l` / `-xl` | General container padding values. |
| `--vaadin-padding-inline-container` | Horizontal padding of a standard one-line container. |
| `--vaadin-padding-block-container` | Vertical padding of a standard one-line container. |

### 3. Sizing, Radii & Interaction
| CSS Custom Property | Default / Purpose |
| :--- | :--- |
| `--vaadin-radius-s` | Border radius for small UI controls. |
| `--vaadin-radius-m` | Default border radius for most components. |
| `--vaadin-radius-l` | Border radius for larger containers (cards, dialogs). |
| `--vaadin-focus-ring-color` | Color of the keyboard focus outline. |
| `--vaadin-focus-ring-width` | Width of the keyboard focus outline. |
| `--vaadin-clickable-cursor` | Cursor for clickable elements (default: `pointer`). |
| `--vaadin-disabled-cursor` | Cursor when hovering over disabled elements. |
| `--vaadin-icon-size` | Space reserved by an icon in the layout. |
| `--vaadin-icon-visual-size` | Visual size of the icon inside its container. |
| `--vaadin-icon-stroke-width` | Stroke width of SVG icons. |

---

## Part 2: Lumo Theme CSS Variables
The default visual design system of Vaadin.

### 1. Typography
| CSS Custom Property | Value / Purpose |
| :--- | :--- |
| `--lumo-font-family` | System font stack. |
| `--lumo-font-size-xxs` to `-xxxl` | Font scale from `0.75rem` (12px) to `2.5rem` (40px). |
| `--lumo-line-height-xs` / `-s` / `-m` | Line heights from `1.25` (headings) to `1.625` (body). |

### 2. Text Colors
| CSS Custom Property | Description |
| :--- | :--- |
| `--lumo-header-text-color` | Heading text (maximum contrast). |
| `--lumo-body-text-color` | Standard body text. |
| `--lumo-secondary-text-color` | Secondary supporting text. |
| `--lumo-tertiary-text-color` | Hints, quiet labels, or icons. |
| `--lumo-disabled-text-color` | Disabled text styles. |

### 3. Brand & Contextual Colors
| CSS Custom Property | Description |
| :--- | :--- |
| `--lumo-base-color` | Main background color (white/dark gray). |
| `--lumo-primary-color` / `-text-color` / `-contrast-color` | Action color (blue) and text variations. |
| `--lumo-error-color` / `-text-color` / `-contrast-color` | Error state color (red) and variations. |
| `--lumo-success-color` / `-text-color` / `-contrast-color` | Success state color (green) and variations. |
| `--lumo-warning-color` / `-text-color` / `-contrast-color` | Warning state color (orange/yellow) and variations. |

### 4. Grayscale & Contrast Overlays
| CSS Custom Property | Description |
| :--- | :--- |
| `--lumo-contrast-5pct` | Secondary section background, button backgrounds. |
| `--lumo-contrast-10pct` | Input field backgrounds. |
| `--lumo-contrast-20pct` | Border & divider lines. |
| `--lumo-contrast-30pct` / `-50pct` / `-70pct` / `-90pct` | Standard opacity gradients. |

### 5. Shapes & Elevation
| CSS Custom Property | Description |
| :--- | :--- |
| `--lumo-border-radius-s` / `-m` / `-l` | Border radii. |
| `--lumo-box-shadow-xs` to `-xl` | Shadow depth values. |

---

## Part 3: Aura Theme CSS Variables
Aura is a modern theme that maps its own color, typography, and shape systems down to the base style properties.

### 1. Aura Color & Contrast
| CSS Custom Property | Description |
| :--- | :--- |
| `--aura-content-color-scheme` | Defines the App Layout content area color scheme (`light`, `dark`, or `light dark`). |
| `--aura-notification-color-scheme` | Color scheme for all notifications. |
| `--aura-background-color-light` / `-dark` | Base background color for the respective color schemes. |
| `--aura-app-background` | The main app background (supports solid colors or gradient values). |
| `--aura-contrast-level` | Number used to scale the contrast of computed text/borders. |
| `--aura-accent-color-light` / `-dark` | The main accent highlight color for light and dark schemes. |
| `--aura-accent-text-color-light` / `-dark` | Text colors derived from the accent color. |
| `--aura-accent-border-color` | Border color slightly tinted with the accent color. |

### 2. Aura Color Palette
The Aura theme provides a set of core palette colors, with auto-computed `-text` variants:
* **Neutral**: `--aura-neutral`, `--aura-neutral-light`, `--aura-neutral-dark`
* **Saturated/Accents**: `--aura-red`, `--aura-orange`, `--aura-yellow`, `--aura-green`, `--aura-blue`, `--aura-purple`
* **Colored Text**: `--aura-red-text`, `--aura-orange-text`, `--aura-yellow-text`, `--aura-green-text`, `--aura-blue-text`, `--aura-purple-text`

### 3. Aura Elevation & Surfaces
| CSS Custom Property | Description |
| :--- | :--- |
| `--aura-surface-color` | A computed elevated background color that adapts to the theme. |
| `--aura-surface-level` | Elevation level (positive/negative fraction). Higher number = higher elevation. |
| `--aura-surface-opacity` | Transparency/opacity of the surface (default `0.5`). |
| `--aura-overlay-surface-opacity` | Surface opacity for overlay elements. |
| `--aura-accent-surface` | Elevated surface tinted with the accent color. |
| `--aura-shadow-xs`, `-s`, `-m` | Shadow levels tailored for different UI elevations. |

### 4. Aura Typography
| CSS Custom Property | Value / Purpose |
| :--- | :--- |
| `--aura-font-family` | Active application font family (defaults to `Instrument Sans` stack). |
| `--aura-base-font-size` | Numeric base size (in pixels) used to scale sizes `xs` through `xl`. |
| `--aura-font-size-xs` to `-xl` | Rounded scale of font sizes. |
| `--aura-base-line-height` | Relative base multiplier used to scale line heights. |
| `--aura-line-height-xs` to `-xl` | Line heights. |
| `--aura-font-weight-regular` / `-medium` / `-semibold` | Font weight values. |

### 5. Aura App Layout Customize
| CSS Custom Property | Description |
| :--- | :--- |
| `--aura-app-layout-inset` | Viewport inset margin (e.g. `12px` or `0px`). |
| `--aura-base-size` | Unitless multiplier (e.g. 12-24) to scale gaps and paddings. |
| `--aura-base-radius` | Unitless multiplier (e.g. 0-10) to scale border radii. |
