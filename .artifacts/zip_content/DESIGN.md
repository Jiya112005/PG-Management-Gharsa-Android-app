---
name: GharSa Mobile Native
colors:
  surface: '#f9f9ff'
  surface-dim: '#cfdaf2'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f0f3ff'
  surface-container: '#e7eeff'
  surface-container-high: '#dee8ff'
  surface-container-highest: '#d8e3fb'
  on-surface: '#111c2d'
  on-surface-variant: '#42474d'
  inverse-surface: '#263143'
  inverse-on-surface: '#ecf1ff'
  outline: '#73777e'
  outline-variant: '#c2c7ce'
  surface-tint: '#40617f'
  primary: '#00253e'
  on-primary: '#ffffff'
  primary-container: '#173b57'
  on-primary-container: '#84a5c6'
  inverse-primary: '#a9caec'
  secondary: '#006a63'
  on-secondary: '#ffffff'
  secondary-container: '#80f6ea'
  on-secondary-container: '#00716a'
  tertiary: '#341e00'
  on-tertiary: '#ffffff'
  tertiary-container: '#523100'
  on-tertiary-container: '#e19000'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#cde5ff'
  primary-fixed-dim: '#a9caec'
  on-primary-fixed: '#001d32'
  on-primary-fixed-variant: '#284966'
  secondary-fixed: '#80f6ea'
  secondary-fixed-dim: '#62d9ce'
  on-secondary-fixed: '#00201d'
  on-secondary-fixed-variant: '#00504b'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#f9f9ff'
  on-background: '#111c2d'
  surface-variant: '#d8e3fb'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 44px
    fontWeight: '700'
    lineHeight: 52px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-lg:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0.01em
  title-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.015em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.015em
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.03em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-sm: 0.75rem
  gutter-lg: 1.5rem
  margin: 1rem
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system drives an Android-native property operations tool engineered specifically for managing Paying Guest (PG) facilities, student hostels, and urban co-living residences. The operational context spans two core user cohorts: busy property operators tracking rent, room inventory, and maintenance tickets on the move; and young tenants interacting with payments, check-ins, and complaint logs.

The visual tone merges institutional trust with residential hospitality. While hospitality demands warm, approachable accessibility, commercial leasing requires rigorous financial clarity and functional speed. The aesthetic builds strictly upon Google Material 3 (Material You) architectural patterns, infusing a customized structural composure that eliminates generic visual sprawl. 

Tactile responsiveness, instant scannability, clear physical tap targets, and thumb-driven ergonomics define the interface. Interactions favor deliberate, restrained surface transitions over ornamental noise.

## Colors

The color architecture enforces operational distinction, fiscal accuracy, and rapid state recognition across high-density operational views.

- **Primary (`#173B57` - Deep Navy Blue)**: Anchor for structural framing, native top AppBars, high-priority primary actions, and key ledger metrics. Communicates reliability, tenure, and contractual security.
- **Primary Container (`#E8EEF3` - Soft Navy Slate)**: Applied to selected bottom navigation items, quiet functional active states, and sub-card highlights without visual vibration.
- **Secondary (`#20A89E` - Vibrant Teal)**: Highlights positive micro-actions, Floating Action Buttons (FABs), verified indicators, and modern hospitality touches.
- **Secondary Container (`#E0F5F3` - Light Teal)**: Subtle background wash for room availability tags, utility receipts, and positive balance pills.
- **Background (`#F5F7FA` - Canvas Light Grey)**: Low-glare foundational canvas that reduces visual fatigue during sustained data entry.
- **Surface & Cards (`#FFFFFF` - Pure White)**: Elevated interactive surfaces, bottom sheets, search modals, and modular booking cards.
- **Text Primary (`#1E293B` - Dark Charcoal)**: High-contrast, WCAG AAA compliant text role for financial figures, occupant names, and critical headers.
- **Text Secondary (`#64748B` - Muted Slate)**: Controlled de-emphasis for room attributes, sub-labels, timestamps, and placeholder metadata.
- **Functional Semantics**:
  - Success (`#10B981`): Paid rent, settled invoices, resolved maintenance issues.
  - Warning (`#F59E0B`): Pending dues, upcoming lease expiries, unconfirmed visitors.
  - Error / Urgent (`#EF4444`): Overdue rent, security breaches, vacant beds past projection, high-priority emergency tickets.

## Typography

The typographic hierarchy follows Android Material Design 3 type specifications, powered by Inter to maximize readability on low-DPI and high-DPI mobile devices alike. 

- **Display & Large Headlines**: Strictly reserved for revenue totals, dashboard occupancy percentages, and onboarding greeting headers.
- **Titles**: Provide crisp sectional demarcation across bed-matrix dashboards, tenant directories, and payment logs.
- **Body Styles**: Tuned with open counterforms and deliberate line heights to maintain effortless scanning during field inspections or tenant verification on compact screens.
- **Labels**: Capitalized or weighted for immediate glanceability inside status badges, input helpers, tabs, and bottom bar destinations. Numbers and financial values must consistently utilize tabular or proportional figures with medium-to-bold weights to avoid layout jitter during dynamic updates.

## Layout & Spacing

The layout philosophy adapts the 8dp Android baseline grid with a 4dp micro-step rhythm. The primary environment is a 4-column fluid mobile grid scaling to an 8-column layout on foldable devices and tablets.

- **Screen Canvas**: Standard mobile layouts enforce a fixed outer screen margin of `16dp` (`1rem`), expanding to `24dp` on small tablets or landscape viewports.
- **Touch Ergonomics**: All interactive elements (list rows, chips, toggles, navigation items) respect the strict 48x48dp native touch target guideline, even when visual bounds are smaller.
- **Vertical Stacking**: Sectional containers, card collections, and form sets enforce an 8dp/16dp vertical cadence. Scrollable lists maintain safe bottom content padding of at least 80dp to ensure full visibility above floating action buttons and the persistent bottom navigation bar.

## Elevation & Depth

Elevation strictly reflects the Android Material 3 tonal and ambient shadow architecture. Depth communicates contextual interaction layers and stacking hierarchies rather than decorative realism.

- **Level 0 (Base Canvas - `#F5F7FA`)**: Flat background canvas, no shadow. Used for global layout wrappers.
- **Level 1 (Cards, List Tiles - `#FFFFFF`)**: Standard surface containers elevated with soft ambient depth: `box-shadow: 0px 1px 3px 0px rgba(23, 59, 87, 0.08), 0px 1px 2px 0px rgba(23, 59, 87, 0.04)`.
- **Level 2 (Top AppBars & Search Filters)**: Elevated on scroll: `box-shadow: 0px 2px 6px 0px rgba(23, 59, 87, 0.10), 0px 1px 2px 0px rgba(23, 59, 87, 0.06)`.
- **Level 3 (Floating Action Button & Bottom Navigation)**: Persistent above canvas: `box-shadow: 0px 4px 12px 0px rgba(23, 59, 87, 0.12), 0px 2px 4px 0px rgba(23, 59, 87, 0.06)`.
- **Level 4 (Bottom Sheets, Dialogs & Date Pickers)**: Interruptive modal surfaces overlaid against a 40% `#173B57` backdrop scrim: `box-shadow: 0px 8px 24px 0px rgba(23, 59, 87, 0.16), 0px 4px 8px 0px rgba(23, 59, 87, 0.08)`.

Shadows are uniquely tinted with Primary Navy (`#173B57`) rather than neutral pitch black, retaining warmth and coherence with the brand identity.

## Shapes

The design system incorporates geometric hierarchy to guide the user's focus through the management flow:

- **16dp Rounded Corners (`rounded-lg`)**: Applied universally to primary surface cards, accommodation overview blocks, bottom sheet top edges, and modal dialogue containers.
- **12dp Rounded Corners**: Standard for all input fields, dropdown menus, date selectors, and nested media thumbnail containers.
- **Full Pill Roundedness (`rounded-full`)**: Reserved exclusively for standard action buttons, filter chips, state badges, and numeric indicator bubbles.
- **16dp Squircle / MD3 Curvature**: Applied to Floating Action Buttons (FAB) following Android 13/14 shape standards.

## Components

### Top AppBar (Native Android)
- **Small / Center-Aligned**: Displays the current view title (18px SemiBold, `#1E293B`) with back navigation, contextual quick-filter icon, and notification bell with an unread badge.
- **Large AppBar**: Used on main landing views (Dashboard, Tenants, Accounting) collapsing into standard height upon upward scroll. Background remains `#F5F7FA` at resting state, tinting subtly with a Level 2 shadow when scrolled.

### Bottom Navigation Bar
- Fixed height of 80dp adhering to MD3. Background in `#FFFFFF` with an upper border line in `#E8EEF3`.
- Active destination is housed within an elongated pill container filled with Primary Container (`#E8EEF3`), containing an active icon rendered in Primary Navy (`#173B57`).
- Inactive items show outlines in Muted Slate (`#64748B`) with `label-sm` text.

### Buttons & Floating Action Button (FAB)
- **Primary Action Buttons**: Full pill shape, solid `#173B57` fill, white `#FFFFFF` text (`label-lg`), 48dp height. Active state transitions to `#112C42`.
- **Secondary / Outlined Buttons**: Full pill shape, 1.5px border in `#173B57`, transparent fill, `#173B57` text.
- **Floating Action Button (FAB)**: 56x56dp, rounded-2xl (16dp corners), background Vibrant Teal (`#20A89E`), icon in `#FFFFFF`. Casts Level 3 elevation. Trigger actions include "Add Tenant", "Record Payment", or "Log Complaint".

### Input Fields & Selectors
- Height of 56dp with 12dp border radius. 
- Background `#FFFFFF`, outline 1.5px `#E8EEF3` in resting state; transitions to 2px solid `#173B57` on focus.
- Floating label shifts from inside placeholder to upper border notch using `label-md` in `#64748B` (resting) or `#173B57` (focused). Error states replace borders with `#EF4444` accompanied by supporting helper text.

### Status Chips & Badges
- 32dp height, full pill shape, internal horizontal padding of 12dp.
- Paired color configurations:
  - *Occupied / Paid*: `#10B981` text over `rgba(16, 185, 129, 0.12)` background.
  - *Due Soon / Reserved*: `#F59E0B` text over `rgba(245, 158, 11, 0.12)` background.
  - *Overdue / Vacant / Urgent*: `#EF4444` text over `rgba(239, 68, 68, 0.12)` background.
  - *Info / Neutral*: `#173B57` text over `#E8EEF3` background.

### Cards & Property Modules
- Elevated with Level 1 shadow, 16dp rounded corners, pure white background.
- Room matrix cards include bed-occupancy indicators, rent due dates, and quick action context menus (three-dot affordance).

### Checkboxes & Radio Controls
- Minimum 48dp tap boundary enclosing a 20dp visual indicator.
- Selected state fills with `#173B57` featuring a pure white check/bullet; unselected displays a 2px `#64748B` boundary outline.