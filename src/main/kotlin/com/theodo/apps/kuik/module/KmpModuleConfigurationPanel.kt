package com.theodo.apps.kuik.module

import com.intellij.util.ui.JBUI
import com.theodo.apps.kuik.module.model.ModuleType
import com.theodo.apps.kuik.module.model.ProjectHelper
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionListener
import javax.swing.Box
import javax.swing.ButtonGroup
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class KmpModuleConfigurationPanel : JPanel() {
    private val defaultPackage = ProjectHelper.getPackage() ?: "com.theodo.example"
    private val packageNameLabel: JLabel = JLabel("Package Name:")
    private val packageNameField: JTextField = JTextField(15)
    private val completePackageNameField: JLabel = JLabel("")

    private val moduleNameLabel: JLabel = JLabel("Module Name:")
    val moduleNameField: JTextField = JTextField(15)
    private val moduleNameErrorLabel: JLabel = JLabel()

    private val includeAndroidCheckBox: JCheckBox = JCheckBox("Include Android")
    private val includeIosCheckBox: JCheckBox = JCheckBox("Include iOS")
    private val includeJvmCheckBox: JCheckBox = JCheckBox("Include JVM")
    private val includeWebCheckBox: JCheckBox = JCheckBox("Include Web")

    // Radio buttons for module type
    private val featureRadioButton: JRadioButton = JRadioButton("Feature")
    private val coreRadioButton: JRadioButton = JRadioButton("Core")
    private val domainRadioButton: JRadioButton = JRadioButton("Domain")
    private val dataRadioButton: JRadioButton = JRadioButton("Data")
    private val moduleTypeGroup: ButtonGroup = ButtonGroup()

    init {

        includeAndroidCheckBox.isSelected = true
        includeIosCheckBox.isSelected = true

        moduleNameField.text = "kmpsharedmodule"
        packageNameField.text = defaultPackage
        updatePackageName()

        moduleNameField.document.addDocumentListener(
            object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) {
                    validateModuleName()
                }

                override fun removeUpdate(e: DocumentEvent?) {
                    validateModuleName()
                }

                override fun changedUpdate(e: DocumentEvent?) {
                    validateModuleName()
                }
            },
        )

        packageNameField.document.addDocumentListener(
            object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) {
                    updatePackageName()
                }

                override fun removeUpdate(e: DocumentEvent?) {
                    updatePackageName()
                }

                override fun changedUpdate(e: DocumentEvent?) {
                    updatePackageName()
                }
            },
        )

        // Add radio buttons to the group
        moduleTypeGroup.add(featureRadioButton)
        moduleTypeGroup.add(coreRadioButton)
        moduleTypeGroup.add(domainRadioButton)
        moduleTypeGroup.add(dataRadioButton)
        moduleTypeGroup.setSelected(featureRadioButton.model, true)
        val moduleTypeListener = ActionListener { updatePackageName() }
        featureRadioButton.addActionListener(moduleTypeListener)
        coreRadioButton.addActionListener(moduleTypeListener)
        domainRadioButton.addActionListener(moduleTypeListener)
        dataRadioButton.addActionListener(moduleTypeListener)

        layout = GridBagLayout()
        val gbc =
            GridBagConstraints().apply {
                insets = JBUI.insets(5, 0)
                fill = GridBagConstraints.HORIZONTAL
                anchor = GridBagConstraints.NORTH
            }

        // Module Name
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.gridwidth = 1
        add(moduleNameLabel, gbc)
        gbc.gridx = 1
        gbc.gridy = 0
        gbc.gridwidth = 2
        add(moduleNameField, gbc)
        gbc.gridx = 1
        gbc.gridy = 1
        gbc.gridwidth = 2
        add(moduleNameErrorLabel, gbc)

        // Package Name
        gbc.gridx = 0
        gbc.gridy = 2
        gbc.gridwidth = 1
        add(packageNameLabel, gbc)
        gbc.gridx = 1
        gbc.gridy = 2
        gbc.gridwidth = 2
        add(packageNameField, gbc)
        gbc.gridx = 1
        gbc.gridy = 3
        gbc.gridwidth = 2
        add(completePackageNameField, gbc)

        // Radio Buttons for Module Type
        gbc.gridx = 0
        gbc.gridy = 4
        gbc.gridwidth = 1
        add(JLabel("Module Type:"), gbc)
        gbc.gridx = 1
        gbc.gridy = 4
        gbc.gridwidth = 1
        add(featureRadioButton, gbc)
        gbc.gridx = 1
        gbc.gridy = 5
        gbc.gridwidth = 1
        add(coreRadioButton, gbc)
        gbc.gridx = 1
        gbc.gridy = 6
        gbc.gridwidth = 1
        add(domainRadioButton, gbc)
        gbc.gridx = 1
        gbc.gridy = 7
        gbc.gridwidth = 1
        add(dataRadioButton, gbc)

        gbc.gridx = 0
        gbc.gridy = 8
        gbc.weighty = 1.0
        add(Box.createVerticalGlue(), gbc)

        moduleNameErrorLabel.isVisible = false
    }

    private fun validateModuleName() {
        val moduleName = moduleNameField.text.trim()
        if (moduleName.isEmpty()) {
            moduleNameErrorLabel.text = "Please enter a valid module name"
            moduleNameErrorLabel.isVisible = true
        } else {
            moduleNameErrorLabel.isVisible = false
        }

        updatePackageName()
    }

    private fun updatePackageName() {
        val moduleName = moduleNameField.text.trim().lowercase()
        val packageName = packageNameField.text.trim().lowercase()
        val moduleType = getModuleType().folderName()
        completePackageNameField.text = "$packageName.$moduleType.$moduleName"
    }

    fun getPackageName(): String = completePackageNameField.text.trim()

    fun getModuleName(): String = moduleNameField.text.trim()

    fun isIncludeAndroid(): Boolean = includeAndroidCheckBox.isSelected

    fun isIncludeIos(): Boolean = includeIosCheckBox.isSelected

    fun isIncludeWeb(): Boolean = includeWebCheckBox.isSelected

    fun isIncludeDesktop(): Boolean = includeJvmCheckBox.isSelected

    fun isIncludeServer(): Boolean = includeJvmCheckBox.isSelected

    fun getModuleType(): ModuleType =
        when {
            featureRadioButton.isSelected -> ModuleType.FEATURE
            coreRadioButton.isSelected -> ModuleType.CORE
            domainRadioButton.isSelected -> ModuleType.DOMAIN
            dataRadioButton.isSelected -> ModuleType.DATA
            else -> ModuleType.FEATURE
        }
}
