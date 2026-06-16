# Changelog

## [2.3.0]

### Changed
- Updated to 26.2

## [2.2.0]

### Added
- Armour Opacity slider for Player Renders hide mode (0% to 100%, with 100% as the default)
- Scope setting to control who armour hiding applies to: **All** (default), **Self**, or **Others**
  - Accessible via the GUI (cycles through options) or `/hiddenarmour scope all|self|others`

### Changed
- Shield hidden toggle was moved into the icon row.

### Fixed
- Fixed keybind category name showing raw translation key (`key.category.hiddenarmour.controls`) instead of a readable label.

## [2.1.0]

### Added
- Shield toggle in Player Renders, while not actively blocking (included in the global toggle keybind)

### Fixed
- Nametag hiding now works correctly for players and other living entities.

## [1.7.1]

### Fixed
- Fixed horse armor toggle incorrectly affecting nautilus armor

### Known Issues
- Horse and nautilus armor toggles will hide both saddles and armor together due to Minecraft rendering limitations

## [1.7.0]

### Added
- Added Nautilus armour to mob renders

### Changed
- Updated to 1.21.11
- Glint now hides all forms of glint, also on the 3D armour pieces

### Fixed
- Fixed nametags not being hidden when the nametags toggle is set to "Hidden"

---
Note: Changelog entries use three sections: Added, Changed, and Fixed. Only sections with real updates should contain bullet points.