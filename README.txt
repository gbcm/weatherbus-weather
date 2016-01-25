To get started:

1. Import the gradle file:
        * Import Project from the intellij splash screen
        * Select build.gradle from the repo root
        * Accept the defaults
2. Fix the project's Java configuration:
        * File -> Project Structure
        * Select Project Settings/Project on the left
        * Change the project language level to at least 8.
3. Tell IntelliJ how to run it:
        * Right click and run application class
        * Run -> Edit Configurations, Select application class
        * Remove intellij 'make' command
        * Add gradle task 'assemble'
4. Tell compiler that lombok is a thing
        * Preferences -> Plugins -> Install
        * Install lombok plugin
