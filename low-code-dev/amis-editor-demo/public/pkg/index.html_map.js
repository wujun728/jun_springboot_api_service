amis.require.resourceMap({
  "res": {
    "loadMonacoEditor.ts": {
      "url": "/loadMonacoEditor.js",
      "type": "js"
    },
    "route/NotFound.tsx": {
      "url": "/route/NotFound.js",
      "type": "js"
    },
    "component/AMISRenderer.tsx": {
      "url": "/component/AMISRenderer.js",
      "type": "js"
    },
    "component/AddPageModal.tsx": {
      "url": "/component/AddPageModal.js",
      "type": "js",
      "deps": [
        "component/AMISRenderer.tsx"
      ]
    },
    "route/Preview.tsx": {
      "url": "/route/Preview.js",
      "type": "js",
      "deps": [
        "route/NotFound.tsx",
        "component/AMISRenderer.tsx",
        "component/AddPageModal.tsx"
      ]
    },
    "node_modules/amis-editor/lib/locale/en-US": {
      "url": "/node_modules/amis-editor/lib/locale/en-US.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/locale/zh-CN": {
      "url": "/node_modules/amis-editor/lib/locale/zh-CN.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/event-control/comp-action-select": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/comp-action-select.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/event-control/helper": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/helper.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/comp-action-select"
      ]
    },
    "node_modules/amis-editor/lib/component/BaseControl": {
      "url": "/node_modules/amis-editor/lib/component/BaseControl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/component/InputComponentName": {
      "url": "/node_modules/amis-editor/lib/component/InputComponentName.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/utils": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/utils.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/FormulaControl": {
      "url": "/node_modules/amis-editor/lib/renderer/FormulaControl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/textarea-formula/utils"
      ]
    },
    "node_modules/amis-editor/lib/tpl/common": {
      "url": "/node_modules/amis-editor/lib/tpl/common.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/amis-editor/lib/component/InputComponentName",
        "node_modules/amis-editor/lib/renderer/FormulaControl"
      ]
    },
    "node_modules/amis-editor/lib/tpl/horizontal": {
      "url": "/node_modules/amis-editor/lib/tpl/horizontal.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/tpl/api": {
      "url": "/node_modules/amis-editor/lib/tpl/api.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/tpl/options": {
      "url": "/node_modules/amis-editor/lib/tpl/options.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/tpl/validations": {
      "url": "/node_modules/amis-editor/lib/tpl/validations.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/tpl/style": {
      "url": "/node_modules/amis-editor/lib/tpl/style.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/tpl/layout": {
      "url": "/node_modules/amis-editor/lib/tpl/layout.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Others/Action": {
      "url": "/node_modules/amis-editor/lib/plugin/Others/Action.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Others/TableCell": {
      "url": "/node_modules/amis-editor/lib/plugin/Others/TableCell.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/InputArray": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputArray.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/ButtonGroupSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ButtonGroupSelect.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ButtonToolbar": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ButtonToolbar.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/validator": {
      "url": "/node_modules/amis-editor/lib/validator.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/ChainedSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ChainedSelect.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Checkbox": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Checkbox.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Checkboxes": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Checkboxes.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputCity": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputCity.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputColor": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputColor.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Combo": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Combo.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ConditionBuilder": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ConditionBuilder.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/Control": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Control.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDate": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDate.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/renderer/FormulaControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDateRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDateRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/renderer/FormulaControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDateTime": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDateTime.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDate"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDateTimeRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDateTimeRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/DiffEditor": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/DiffEditor.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/CodeEditor": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/CodeEditor.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputText": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputText.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputEmail": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputEmail.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputText"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/FieldSet": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/FieldSet.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/InputFile": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputFile.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Form": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Form.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Formula": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Formula.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/Group": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Group.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/Hidden": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Hidden.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/InputImage": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputImage.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputGroup": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputGroup.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Item": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Item.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ListSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ListSelect.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/LocationPicker": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/LocationPicker.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/UUID": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/UUID.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/MatrixCheckboxes": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/MatrixCheckboxes.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputMonth": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputMonth.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDate"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputMonthRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputMonthRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/NestedSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/NestedSelect.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputNumber": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputNumber.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputPassword": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputPassword.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputText"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputQuarter": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputQuarter.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDate"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputQuarterRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputQuarterRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Picker": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Picker.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Radios": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Radios.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRating": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRating.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRepeat": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRepeat.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRichText": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRichText.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/Select": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Select.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Static": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Static.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/InputSubForm": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputSubForm.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/Switch": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Switch.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTable": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTable.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTag": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTag.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/TabsTransfer": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/TabsTransfer.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Textarea": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Textarea.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Transfer": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Transfer.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTime": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTime.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDate"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTimeRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTimeRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTree": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTree.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/TreeSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/TreeSelect.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputURL": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputURL.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputText"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputYear": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputYear.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDate"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputYearRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputYearRange.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputExcel": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputExcel.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputKV": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputKV.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Alert": {
      "url": "/node_modules/amis-editor/lib/plugin/Alert.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Audio": {
      "url": "/node_modules/amis-editor/lib/plugin/Audio.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Avatar": {
      "url": "/node_modules/amis-editor/lib/plugin/Avatar.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Button": {
      "url": "/node_modules/amis-editor/lib/plugin/Button.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/ButtonGroup": {
      "url": "/node_modules/amis-editor/lib/plugin/ButtonGroup.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Breadcrumb": {
      "url": "/node_modules/amis-editor/lib/plugin/Breadcrumb.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Card": {
      "url": "/node_modules/amis-editor/lib/plugin/Card.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Card2": {
      "url": "/node_modules/amis-editor/lib/plugin/Card2.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Cards": {
      "url": "/node_modules/amis-editor/lib/plugin/Cards.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Carousel": {
      "url": "/node_modules/amis-editor/lib/plugin/Carousel.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Chart": {
      "url": "/node_modules/amis-editor/lib/plugin/Chart.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Collapse": {
      "url": "/node_modules/amis-editor/lib/plugin/Collapse.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/CollapseGroup": {
      "url": "/node_modules/amis-editor/lib/plugin/CollapseGroup.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Container": {
      "url": "/node_modules/amis-editor/lib/plugin/Container.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/CRUD": {
      "url": "/node_modules/amis-editor/lib/plugin/CRUD.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Custom": {
      "url": "/node_modules/amis-editor/lib/plugin/Custom.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/CustomRegion": {
      "url": "/node_modules/amis-editor/lib/plugin/CustomRegion.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Date": {
      "url": "/node_modules/amis-editor/lib/plugin/Date.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Datetime": {
      "url": "/node_modules/amis-editor/lib/plugin/Datetime.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Date"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Dialog": {
      "url": "/node_modules/amis-editor/lib/plugin/Dialog.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Divider": {
      "url": "/node_modules/amis-editor/lib/plugin/Divider.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Drawer": {
      "url": "/node_modules/amis-editor/lib/plugin/Drawer.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/plugin/Dialog"
      ]
    },
    "node_modules/amis-editor/lib/plugin/DropDownButton": {
      "url": "/node_modules/amis-editor/lib/plugin/DropDownButton.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Each": {
      "url": "/node_modules/amis-editor/lib/plugin/Each.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Flex": {
      "url": "/node_modules/amis-editor/lib/plugin/Flex.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Grid": {
      "url": "/node_modules/amis-editor/lib/plugin/Grid.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/HBox": {
      "url": "/node_modules/amis-editor/lib/plugin/HBox.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/IFrame": {
      "url": "/node_modules/amis-editor/lib/plugin/IFrame.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Image": {
      "url": "/node_modules/amis-editor/lib/plugin/Image.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Images": {
      "url": "/node_modules/amis-editor/lib/plugin/Images.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Json": {
      "url": "/node_modules/amis-editor/lib/plugin/Json.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Link": {
      "url": "/node_modules/amis-editor/lib/plugin/Link.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/List": {
      "url": "/node_modules/amis-editor/lib/plugin/List.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/ListItem": {
      "url": "/node_modules/amis-editor/lib/plugin/ListItem.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Log": {
      "url": "/node_modules/amis-editor/lib/plugin/Log.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Mapping": {
      "url": "/node_modules/amis-editor/lib/plugin/Mapping.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Markdown": {
      "url": "/node_modules/amis-editor/lib/plugin/Markdown.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Nav": {
      "url": "/node_modules/amis-editor/lib/plugin/Nav.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Operation": {
      "url": "/node_modules/amis-editor/lib/plugin/Operation.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Page": {
      "url": "/node_modules/amis-editor/lib/plugin/Page.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Pagination": {
      "url": "/node_modules/amis-editor/lib/plugin/Pagination.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Panel": {
      "url": "/node_modules/amis-editor/lib/plugin/Panel.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Plain": {
      "url": "/node_modules/amis-editor/lib/plugin/Plain.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Progress": {
      "url": "/node_modules/amis-editor/lib/plugin/Progress.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Property": {
      "url": "/node_modules/amis-editor/lib/plugin/Property.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/QRCode": {
      "url": "/node_modules/amis-editor/lib/plugin/QRCode.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Reset": {
      "url": "/node_modules/amis-editor/lib/plugin/Reset.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Button"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Service": {
      "url": "/node_modules/amis-editor/lib/plugin/Service.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Status": {
      "url": "/node_modules/amis-editor/lib/plugin/Status.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Steps": {
      "url": "/node_modules/amis-editor/lib/plugin/Steps.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Sparkline": {
      "url": "/node_modules/amis-editor/lib/plugin/Sparkline.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Submit": {
      "url": "/node_modules/amis-editor/lib/plugin/Submit.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Button"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Table": {
      "url": "/node_modules/amis-editor/lib/plugin/Table.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Table2": {
      "url": "/node_modules/amis-editor/lib/plugin/Table2.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/TableCell2": {
      "url": "/node_modules/amis-editor/lib/plugin/TableCell2.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/BaseControl"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tabs": {
      "url": "/node_modules/amis-editor/lib/plugin/Tabs.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tasks": {
      "url": "/node_modules/amis-editor/lib/plugin/Tasks.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Time": {
      "url": "/node_modules/amis-editor/lib/plugin/Time.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Date"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Timeline": {
      "url": "/node_modules/amis-editor/lib/plugin/Timeline.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Tpl": {
      "url": "/node_modules/amis-editor/lib/plugin/Tpl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tag": {
      "url": "/node_modules/amis-editor/lib/plugin/Tag.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/AnchorNav": {
      "url": "/node_modules/amis-editor/lib/plugin/AnchorNav.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Video": {
      "url": "/node_modules/amis-editor/lib/plugin/Video.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Wizard": {
      "url": "/node_modules/amis-editor/lib/plugin/Wizard.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Wrapper": {
      "url": "/node_modules/amis-editor/lib/plugin/Wrapper.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/TooltipWrapper": {
      "url": "/node_modules/amis-editor/lib/plugin/TooltipWrapper.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/component/TableViewEditor": {
      "url": "/node_modules/amis-editor/lib/component/TableViewEditor.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/TableView": {
      "url": "/node_modules/amis-editor/lib/plugin/TableView.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/component/TableViewEditor"
      ]
    },
    "node_modules/amis-editor/lib/plugin/CodeView": {
      "url": "/node_modules/amis-editor/lib/plugin/CodeView.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/WebComponent": {
      "url": "/node_modules/amis-editor/lib/plugin/WebComponent.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/CRUD2": {
      "url": "/node_modules/amis-editor/lib/plugin/CRUD2.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/plugin/ColumnToggler": {
      "url": "/node_modules/amis-editor/lib/plugin/ColumnToggler.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout_sorption_container": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout_sorption_container.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout_fixed": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout_fixed.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout1_2_v4": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout1_2_v4.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout_free_container": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout_free_container.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/plugin/Container"
      ]
    },
    "node_modules/amis-editor/lib/renderer/OptionControl": {
      "url": "/node_modules/amis-editor/lib/renderer/OptionControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/MapSourceControl": {
      "url": "/node_modules/amis-editor/lib/renderer/MapSourceControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/TimelineItemControl": {
      "url": "/node_modules/amis-editor/lib/renderer/TimelineItemControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/APIControl": {
      "url": "/node_modules/amis-editor/lib/renderer/APIControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/ValidationItem": {
      "url": "/node_modules/amis-editor/lib/renderer/ValidationItem.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/ValidationControl": {
      "url": "/node_modules/amis-editor/lib/renderer/ValidationControl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/ValidationItem",
        "node_modules/amis-editor/lib/validator"
      ]
    },
    "node_modules/amis-editor/lib/renderer/SwitchMoreControl": {
      "url": "/node_modules/amis-editor/lib/renderer/SwitchMoreControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/StatusControl": {
      "url": "/node_modules/amis-editor/lib/renderer/StatusControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/ExpressionFormulaControl": {
      "url": "/node_modules/amis-editor/lib/renderer/ExpressionFormulaControl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/textarea-formula/utils"
      ]
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/plugin": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/plugin.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/FormulaPicker": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/FormulaPicker.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/TextareaFormulaControl": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/TextareaFormulaControl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/textarea-formula/plugin",
        "node_modules/amis-editor/lib/renderer/textarea-formula/FormulaPicker",
        "node_modules/amis-editor/lib/renderer/textarea-formula/utils"
      ]
    },
    "node_modules/amis-editor/lib/renderer/DateShortCutControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DateShortCutControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/BadgeControl": {
      "url": "/node_modules/amis-editor/lib/renderer/BadgeControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/util": {
      "url": "/node_modules/amis-editor/lib/util.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/BoxModel": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/BoxModel.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/util"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/font-family": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/font-family.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/style-control/Font": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Font.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/style-control/font-family"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/Border": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Border.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/style-control/transformation": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/transformation.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/style-control/BoxShadow": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/BoxShadow.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/style-control/transformation"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/Background": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Background.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/style-control/Display": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Display.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/style-control/InsetBoxModel": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/InsetBoxModel.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/util"
      ]
    },
    "node_modules/amis-editor/lib/renderer/RangePartsControl": {
      "url": "/node_modules/amis-editor/lib/renderer/RangePartsControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/DataBindingControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DataBindingControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/DataMappingControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DataMappingControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/DataPickerControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DataPickerControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/GoConfigControl": {
      "url": "/node_modules/amis-editor/lib/renderer/GoConfigControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/FeatureControl": {
      "url": "/node_modules/amis-editor/lib/renderer/FeatureControl.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/GoConfigControl"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/action-config-panel": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/action-config-panel.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/action-config-dialog": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/action-config-dialog.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/action-config-panel",
        "node_modules/amis-editor/lib/renderer/event-control/comp-action-select",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/index": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/index.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/renderer/event-control/action-config-dialog",
        "node_modules/amis-editor/lib/renderer/event-control/helper"
      ]
    },
    "node_modules/amis-editor/lib/renderer/TreeOptionControl": {
      "url": "/node_modules/amis-editor/lib/renderer/TreeOptionControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/TransferTableControl": {
      "url": "/node_modules/amis-editor/lib/renderer/TransferTableControl.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/renderer/style-control/ThemeClassName": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/ThemeClassName.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/audio.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/audio.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/custom.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/custom.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/each.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/each.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/form.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/form.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/nav.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/nav.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/qrcode.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/qrcode.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/service.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/service.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/table.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/table.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/tasks.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/tasks.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/video.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/video.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/feat/wizard.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/wizard.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/anchor-nav.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/anchor-nav.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/collapse.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/collapse.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/container.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/container.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/flex-container.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/flex-container.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/form-group.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/form-group.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/grid.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/grid.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/iframe.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/iframe.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/panel.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/panel.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/table-view.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/table-view.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/tabs.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/tabs.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/web-component.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/web-component.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/container/tooltip-wrapper.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/tooltip-wrapper.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/btn/btn-group.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/btn-group.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/btn/btn-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/btn-select.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/btn/btn-toolbar.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/btn-toolbar.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/btn/button.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/button.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/btn/dropdown-btn.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/dropdown-btn.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/breadcrumb.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/breadcrumb.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/card.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/card.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/cards.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/cards.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/carousel.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/carousel.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/chart.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/chart.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/code.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/code.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/date.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/date.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/datetime.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/datetime.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/time.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/time.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/image.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/image.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/images.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/images.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/list.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/list.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/log.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/log.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/mapping.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/mapping.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/avatar.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/avatar.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/sparkline.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/sparkline.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/progress.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/progress.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/json-view.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/json-view.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/markdown.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/markdown.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/steps.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/steps.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/status.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/status.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/plain.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/plain.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/show/URL.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/URL.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/chained-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/chained-select.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/checkbox.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/checkbox.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/checkboxes.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/checkboxes.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/combo.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/combo.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/condition-builder.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/condition-builder.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/diff-editor.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/diff-editor.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/editor.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/editor.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/hidden.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/hidden.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-city.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-city.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-color.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-color.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-time-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-time-range.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-date-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-date-range.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-month-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-month-range.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-quarter-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-quarter-range.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-date.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-date.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-datetime.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-datetime.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-email.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-email.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-excel.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-excel.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-file.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-file.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-group.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-group.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-image.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-image.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-kv.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-kv.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-number.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-number.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-password.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-password.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-range.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-rating.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-rating.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-repeat.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-repeat.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-rich-text.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-rich-text.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-tag.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-tag.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-text.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-text.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-time.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-time.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-tree.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-tree.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-url.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-url.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/input-year.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-year.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/list-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/list-select.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/location-picker.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/location-picker.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/matrix-checkboxes.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/matrix-checkboxes.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/month.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/month.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/nested-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/nested-select.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/picker.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/picker.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/quarter.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/quarter.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/radios.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/radios.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/select.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/static.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/static.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/sub-form.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/sub-form.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/switch.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/switch.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/tabs-transfer.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/tabs-transfer.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/textarea.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/textarea.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/transfer.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/transfer.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/tree-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/tree-select.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/uuid.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/uuid.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/formula.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/formula.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/form/inputArray.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/inputArray.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/other/property-sheet.svg": {
      "url": "/node_modules/amis-editor/lib/icons/other/property-sheet.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/other/tooltip.svg": {
      "url": "/node_modules/amis-editor/lib/icons/other/tooltip.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/other/divider.svg": {
      "url": "/node_modules/amis-editor/lib/icons/other/divider.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-absolute.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-absolute.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-fixed.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-fixed.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-1with2.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-1with2.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-2cols.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-2cols.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-2row.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-2row.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-2with1.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-2with1.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-3cols.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-3cols.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-3row.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-3row.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout-full.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-full.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout1-2.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout1-2.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout2-1.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout2-1.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout3-1.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout3-1.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/layout/layout3-2.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout3-2.svg.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/icons/index": {
      "url": "/node_modules/amis-editor/lib/icons/index.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/icons/feat/audio.svg",
        "node_modules/amis-editor/lib/icons/feat/custom.svg",
        "node_modules/amis-editor/lib/icons/feat/each.svg",
        "node_modules/amis-editor/lib/icons/feat/form.svg",
        "node_modules/amis-editor/lib/icons/feat/nav.svg",
        "node_modules/amis-editor/lib/icons/feat/qrcode.svg",
        "node_modules/amis-editor/lib/icons/feat/service.svg",
        "node_modules/amis-editor/lib/icons/feat/table.svg",
        "node_modules/amis-editor/lib/icons/feat/tasks.svg",
        "node_modules/amis-editor/lib/icons/feat/video.svg",
        "node_modules/amis-editor/lib/icons/feat/wizard.svg",
        "node_modules/amis-editor/lib/icons/container/anchor-nav.svg",
        "node_modules/amis-editor/lib/icons/container/collapse.svg",
        "node_modules/amis-editor/lib/icons/container/container.svg",
        "node_modules/amis-editor/lib/icons/container/flex-container.svg",
        "node_modules/amis-editor/lib/icons/container/form-group.svg",
        "node_modules/amis-editor/lib/icons/container/grid.svg",
        "node_modules/amis-editor/lib/icons/container/iframe.svg",
        "node_modules/amis-editor/lib/icons/container/panel.svg",
        "node_modules/amis-editor/lib/icons/container/table-view.svg",
        "node_modules/amis-editor/lib/icons/container/tabs.svg",
        "node_modules/amis-editor/lib/icons/container/web-component.svg",
        "node_modules/amis-editor/lib/icons/container/tooltip-wrapper.svg",
        "node_modules/amis-editor/lib/icons/btn/btn-group.svg",
        "node_modules/amis-editor/lib/icons/btn/btn-select.svg",
        "node_modules/amis-editor/lib/icons/btn/btn-toolbar.svg",
        "node_modules/amis-editor/lib/icons/btn/button.svg",
        "node_modules/amis-editor/lib/icons/btn/dropdown-btn.svg",
        "node_modules/amis-editor/lib/icons/show/breadcrumb.svg",
        "node_modules/amis-editor/lib/icons/show/card.svg",
        "node_modules/amis-editor/lib/icons/show/cards.svg",
        "node_modules/amis-editor/lib/icons/show/carousel.svg",
        "node_modules/amis-editor/lib/icons/show/chart.svg",
        "node_modules/amis-editor/lib/icons/show/code.svg",
        "node_modules/amis-editor/lib/icons/show/date.svg",
        "node_modules/amis-editor/lib/icons/show/datetime.svg",
        "node_modules/amis-editor/lib/icons/show/time.svg",
        "node_modules/amis-editor/lib/icons/show/image.svg",
        "node_modules/amis-editor/lib/icons/show/images.svg",
        "node_modules/amis-editor/lib/icons/show/list.svg",
        "node_modules/amis-editor/lib/icons/show/log.svg",
        "node_modules/amis-editor/lib/icons/show/mapping.svg",
        "node_modules/amis-editor/lib/icons/show/avatar.svg",
        "node_modules/amis-editor/lib/icons/show/sparkline.svg",
        "node_modules/amis-editor/lib/icons/show/progress.svg",
        "node_modules/amis-editor/lib/icons/show/json-view.svg",
        "node_modules/amis-editor/lib/icons/show/markdown.svg",
        "node_modules/amis-editor/lib/icons/show/steps.svg",
        "node_modules/amis-editor/lib/icons/show/status.svg",
        "node_modules/amis-editor/lib/icons/show/plain.svg",
        "node_modules/amis-editor/lib/icons/show/URL.svg",
        "node_modules/amis-editor/lib/icons/form/chained-select.svg",
        "node_modules/amis-editor/lib/icons/form/checkbox.svg",
        "node_modules/amis-editor/lib/icons/form/checkboxes.svg",
        "node_modules/amis-editor/lib/icons/form/combo.svg",
        "node_modules/amis-editor/lib/icons/form/condition-builder.svg",
        "node_modules/amis-editor/lib/icons/form/diff-editor.svg",
        "node_modules/amis-editor/lib/icons/form/editor.svg",
        "node_modules/amis-editor/lib/icons/form/hidden.svg",
        "node_modules/amis-editor/lib/icons/form/input-city.svg",
        "node_modules/amis-editor/lib/icons/form/input-color.svg",
        "node_modules/amis-editor/lib/icons/form/input-time-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-date-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-month-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-quarter-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-date.svg",
        "node_modules/amis-editor/lib/icons/form/input-datetime.svg",
        "node_modules/amis-editor/lib/icons/form/input-email.svg",
        "node_modules/amis-editor/lib/icons/form/input-excel.svg",
        "node_modules/amis-editor/lib/icons/form/input-file.svg",
        "node_modules/amis-editor/lib/icons/form/input-group.svg",
        "node_modules/amis-editor/lib/icons/form/input-image.svg",
        "node_modules/amis-editor/lib/icons/form/input-kv.svg",
        "node_modules/amis-editor/lib/icons/form/input-number.svg",
        "node_modules/amis-editor/lib/icons/form/input-password.svg",
        "node_modules/amis-editor/lib/icons/form/input-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-rating.svg",
        "node_modules/amis-editor/lib/icons/form/input-repeat.svg",
        "node_modules/amis-editor/lib/icons/form/input-rich-text.svg",
        "node_modules/amis-editor/lib/icons/form/input-tag.svg",
        "node_modules/amis-editor/lib/icons/form/input-text.svg",
        "node_modules/amis-editor/lib/icons/form/input-time.svg",
        "node_modules/amis-editor/lib/icons/form/input-tree.svg",
        "node_modules/amis-editor/lib/icons/form/input-url.svg",
        "node_modules/amis-editor/lib/icons/form/input-year.svg",
        "node_modules/amis-editor/lib/icons/form/list-select.svg",
        "node_modules/amis-editor/lib/icons/form/location-picker.svg",
        "node_modules/amis-editor/lib/icons/form/matrix-checkboxes.svg",
        "node_modules/amis-editor/lib/icons/form/month.svg",
        "node_modules/amis-editor/lib/icons/form/nested-select.svg",
        "node_modules/amis-editor/lib/icons/form/picker.svg",
        "node_modules/amis-editor/lib/icons/form/quarter.svg",
        "node_modules/amis-editor/lib/icons/form/radios.svg",
        "node_modules/amis-editor/lib/icons/form/select.svg",
        "node_modules/amis-editor/lib/icons/form/static.svg",
        "node_modules/amis-editor/lib/icons/form/sub-form.svg",
        "node_modules/amis-editor/lib/icons/form/switch.svg",
        "node_modules/amis-editor/lib/icons/form/tabs-transfer.svg",
        "node_modules/amis-editor/lib/icons/form/textarea.svg",
        "node_modules/amis-editor/lib/icons/form/transfer.svg",
        "node_modules/amis-editor/lib/icons/form/tree-select.svg",
        "node_modules/amis-editor/lib/icons/form/uuid.svg",
        "node_modules/amis-editor/lib/icons/form/formula.svg",
        "node_modules/amis-editor/lib/icons/form/inputArray.svg",
        "node_modules/amis-editor/lib/icons/other/property-sheet.svg",
        "node_modules/amis-editor/lib/icons/other/tooltip.svg",
        "node_modules/amis-editor/lib/icons/other/divider.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-absolute.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-fixed.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-1with2.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-2cols.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-2row.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-2with1.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-3cols.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-3row.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-full.svg",
        "node_modules/amis-editor/lib/icons/layout/layout1-2.svg",
        "node_modules/amis-editor/lib/icons/layout/layout2-1.svg",
        "node_modules/amis-editor/lib/icons/layout/layout3-1.svg",
        "node_modules/amis-editor/lib/icons/layout/layout3-2.svg"
      ]
    },
    "node_modules/amis-editor/lib/index": {
      "url": "/node_modules/amis-editor/lib/index.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/locale/en-US",
        "node_modules/amis-editor/lib/locale/zh-CN",
        "node_modules/amis-editor/lib/tpl/common",
        "node_modules/amis-editor/lib/tpl/horizontal",
        "node_modules/amis-editor/lib/tpl/api",
        "node_modules/amis-editor/lib/tpl/options",
        "node_modules/amis-editor/lib/tpl/validations",
        "node_modules/amis-editor/lib/tpl/style",
        "node_modules/amis-editor/lib/tpl/layout",
        "node_modules/amis-editor/lib/plugin/Others/Action",
        "node_modules/amis-editor/lib/plugin/Others/TableCell",
        "node_modules/amis-editor/lib/plugin/Form/InputArray",
        "node_modules/amis-editor/lib/plugin/Form/ButtonGroupSelect",
        "node_modules/amis-editor/lib/plugin/Form/ButtonToolbar",
        "node_modules/amis-editor/lib/plugin/Form/ChainedSelect",
        "node_modules/amis-editor/lib/plugin/Form/Checkbox",
        "node_modules/amis-editor/lib/plugin/Form/Checkboxes",
        "node_modules/amis-editor/lib/plugin/Form/InputCity",
        "node_modules/amis-editor/lib/plugin/Form/InputColor",
        "node_modules/amis-editor/lib/plugin/Form/Combo",
        "node_modules/amis-editor/lib/plugin/Form/ConditionBuilder",
        "node_modules/amis-editor/lib/plugin/Form/Control",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/amis-editor/lib/plugin/Form/InputDateTime",
        "node_modules/amis-editor/lib/plugin/Form/InputDateTimeRange",
        "node_modules/amis-editor/lib/plugin/Form/DiffEditor",
        "node_modules/amis-editor/lib/plugin/Form/CodeEditor",
        "node_modules/amis-editor/lib/plugin/Form/InputEmail",
        "node_modules/amis-editor/lib/plugin/Form/FieldSet",
        "node_modules/amis-editor/lib/plugin/Form/InputFile",
        "node_modules/amis-editor/lib/plugin/Form/Form",
        "node_modules/amis-editor/lib/plugin/Form/Formula",
        "node_modules/amis-editor/lib/plugin/Form/Group",
        "node_modules/amis-editor/lib/plugin/Form/Hidden",
        "node_modules/amis-editor/lib/plugin/Form/InputImage",
        "node_modules/amis-editor/lib/plugin/Form/InputGroup",
        "node_modules/amis-editor/lib/plugin/Form/Item",
        "node_modules/amis-editor/lib/plugin/Form/ListSelect",
        "node_modules/amis-editor/lib/plugin/Form/LocationPicker",
        "node_modules/amis-editor/lib/plugin/Form/UUID",
        "node_modules/amis-editor/lib/plugin/Form/MatrixCheckboxes",
        "node_modules/amis-editor/lib/plugin/Form/InputMonth",
        "node_modules/amis-editor/lib/plugin/Form/InputMonthRange",
        "node_modules/amis-editor/lib/plugin/Form/NestedSelect",
        "node_modules/amis-editor/lib/plugin/Form/InputNumber",
        "node_modules/amis-editor/lib/plugin/Form/InputPassword",
        "node_modules/amis-editor/lib/plugin/Form/InputQuarter",
        "node_modules/amis-editor/lib/plugin/Form/InputQuarterRange",
        "node_modules/amis-editor/lib/plugin/Form/Picker",
        "node_modules/amis-editor/lib/plugin/Form/Radios",
        "node_modules/amis-editor/lib/plugin/Form/InputRange",
        "node_modules/amis-editor/lib/plugin/Form/InputRating",
        "node_modules/amis-editor/lib/plugin/Form/InputRepeat",
        "node_modules/amis-editor/lib/plugin/Form/InputRichText",
        "node_modules/amis-editor/lib/plugin/Form/Select",
        "node_modules/amis-editor/lib/plugin/Form/Static",
        "node_modules/amis-editor/lib/plugin/Form/InputSubForm",
        "node_modules/amis-editor/lib/plugin/Form/Switch",
        "node_modules/amis-editor/lib/plugin/Form/InputTable",
        "node_modules/amis-editor/lib/plugin/Form/InputTag",
        "node_modules/amis-editor/lib/plugin/Form/InputText",
        "node_modules/amis-editor/lib/plugin/Form/TabsTransfer",
        "node_modules/amis-editor/lib/plugin/Form/Textarea",
        "node_modules/amis-editor/lib/plugin/Form/Transfer",
        "node_modules/amis-editor/lib/plugin/Form/InputTime",
        "node_modules/amis-editor/lib/plugin/Form/InputTimeRange",
        "node_modules/amis-editor/lib/plugin/Form/InputTree",
        "node_modules/amis-editor/lib/plugin/Form/TreeSelect",
        "node_modules/amis-editor/lib/plugin/Form/InputURL",
        "node_modules/amis-editor/lib/plugin/Form/InputYear",
        "node_modules/amis-editor/lib/plugin/Form/InputYearRange",
        "node_modules/amis-editor/lib/plugin/Form/InputExcel",
        "node_modules/amis-editor/lib/plugin/Form/InputKV",
        "node_modules/amis-editor/lib/plugin/Alert",
        "node_modules/amis-editor/lib/plugin/Audio",
        "node_modules/amis-editor/lib/plugin/Avatar",
        "node_modules/amis-editor/lib/plugin/Button",
        "node_modules/amis-editor/lib/plugin/ButtonGroup",
        "node_modules/amis-editor/lib/plugin/Breadcrumb",
        "node_modules/amis-editor/lib/plugin/Card",
        "node_modules/amis-editor/lib/plugin/Card2",
        "node_modules/amis-editor/lib/plugin/Cards",
        "node_modules/amis-editor/lib/plugin/Carousel",
        "node_modules/amis-editor/lib/plugin/Chart",
        "node_modules/amis-editor/lib/plugin/Collapse",
        "node_modules/amis-editor/lib/plugin/CollapseGroup",
        "node_modules/amis-editor/lib/plugin/Container",
        "node_modules/amis-editor/lib/plugin/CRUD",
        "node_modules/amis-editor/lib/plugin/Custom",
        "node_modules/amis-editor/lib/plugin/CustomRegion",
        "node_modules/amis-editor/lib/plugin/Date",
        "node_modules/amis-editor/lib/plugin/Datetime",
        "node_modules/amis-editor/lib/plugin/Dialog",
        "node_modules/amis-editor/lib/plugin/Divider",
        "node_modules/amis-editor/lib/plugin/Drawer",
        "node_modules/amis-editor/lib/plugin/DropDownButton",
        "node_modules/amis-editor/lib/plugin/Each",
        "node_modules/amis-editor/lib/plugin/Flex",
        "node_modules/amis-editor/lib/plugin/Grid",
        "node_modules/amis-editor/lib/plugin/HBox",
        "node_modules/amis-editor/lib/plugin/IFrame",
        "node_modules/amis-editor/lib/plugin/Image",
        "node_modules/amis-editor/lib/plugin/Images",
        "node_modules/amis-editor/lib/plugin/Json",
        "node_modules/amis-editor/lib/plugin/Link",
        "node_modules/amis-editor/lib/plugin/List",
        "node_modules/amis-editor/lib/plugin/ListItem",
        "node_modules/amis-editor/lib/plugin/Log",
        "node_modules/amis-editor/lib/plugin/Mapping",
        "node_modules/amis-editor/lib/plugin/Markdown",
        "node_modules/amis-editor/lib/plugin/Nav",
        "node_modules/amis-editor/lib/plugin/Operation",
        "node_modules/amis-editor/lib/plugin/Page",
        "node_modules/amis-editor/lib/plugin/Pagination",
        "node_modules/amis-editor/lib/plugin/Panel",
        "node_modules/amis-editor/lib/plugin/Plain",
        "node_modules/amis-editor/lib/plugin/Progress",
        "node_modules/amis-editor/lib/plugin/Property",
        "node_modules/amis-editor/lib/plugin/QRCode",
        "node_modules/amis-editor/lib/plugin/Reset",
        "node_modules/amis-editor/lib/plugin/Service",
        "node_modules/amis-editor/lib/plugin/Status",
        "node_modules/amis-editor/lib/plugin/Steps",
        "node_modules/amis-editor/lib/plugin/Sparkline",
        "node_modules/amis-editor/lib/plugin/Submit",
        "node_modules/amis-editor/lib/plugin/Table",
        "node_modules/amis-editor/lib/plugin/Table2",
        "node_modules/amis-editor/lib/plugin/TableCell2",
        "node_modules/amis-editor/lib/plugin/Tabs",
        "node_modules/amis-editor/lib/plugin/Tasks",
        "node_modules/amis-editor/lib/plugin/Time",
        "node_modules/amis-editor/lib/plugin/Timeline",
        "node_modules/amis-editor/lib/plugin/Tpl",
        "node_modules/amis-editor/lib/plugin/Tag",
        "node_modules/amis-editor/lib/plugin/AnchorNav",
        "node_modules/amis-editor/lib/plugin/Video",
        "node_modules/amis-editor/lib/plugin/Wizard",
        "node_modules/amis-editor/lib/plugin/Wrapper",
        "node_modules/amis-editor/lib/plugin/TooltipWrapper",
        "node_modules/amis-editor/lib/plugin/TableView",
        "node_modules/amis-editor/lib/plugin/CodeView",
        "node_modules/amis-editor/lib/plugin/WebComponent",
        "node_modules/amis-editor/lib/plugin/CRUD2",
        "node_modules/amis-editor/lib/plugin/ColumnToggler",
        "node_modules/amis-editor/lib/plugin/Layout/Layout_sorption_container",
        "node_modules/amis-editor/lib/plugin/Layout/Layout_fixed",
        "node_modules/amis-editor/lib/plugin/Layout/Layout1_2_v4",
        "node_modules/amis-editor/lib/plugin/Layout/Layout_free_container",
        "node_modules/amis-editor/lib/renderer/OptionControl",
        "node_modules/amis-editor/lib/renderer/MapSourceControl",
        "node_modules/amis-editor/lib/renderer/TimelineItemControl",
        "node_modules/amis-editor/lib/renderer/APIControl",
        "node_modules/amis-editor/lib/renderer/ValidationControl",
        "node_modules/amis-editor/lib/renderer/ValidationItem",
        "node_modules/amis-editor/lib/renderer/SwitchMoreControl",
        "node_modules/amis-editor/lib/renderer/StatusControl",
        "node_modules/amis-editor/lib/renderer/FormulaControl",
        "node_modules/amis-editor/lib/renderer/ExpressionFormulaControl",
        "node_modules/amis-editor/lib/renderer/textarea-formula/TextareaFormulaControl",
        "node_modules/amis-editor/lib/renderer/DateShortCutControl",
        "node_modules/amis-editor/lib/renderer/BadgeControl",
        "node_modules/amis-editor/lib/renderer/style-control/BoxModel",
        "node_modules/amis-editor/lib/renderer/style-control/Font",
        "node_modules/amis-editor/lib/renderer/style-control/Border",
        "node_modules/amis-editor/lib/renderer/style-control/BoxShadow",
        "node_modules/amis-editor/lib/renderer/style-control/Background",
        "node_modules/amis-editor/lib/renderer/style-control/Display",
        "node_modules/amis-editor/lib/renderer/style-control/InsetBoxModel",
        "node_modules/amis-editor/lib/renderer/RangePartsControl",
        "node_modules/amis-editor/lib/renderer/DataBindingControl",
        "node_modules/amis-editor/lib/renderer/DataMappingControl",
        "node_modules/amis-editor/lib/renderer/DataPickerControl",
        "node_modules/amis-editor/lib/renderer/FeatureControl",
        "node_modules/amis-editor/lib/renderer/event-control/index",
        "node_modules/amis-editor/lib/renderer/TreeOptionControl",
        "node_modules/amis-editor/lib/renderer/TransferTableControl",
        "node_modules/amis-editor/lib/renderer/style-control/ThemeClassName",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/amis-editor/lib/icons/index"
      ]
    },
    "icons/pc-preview.svg": {
      "url": "/icons/pc-preview.js",
      "type": "js"
    },
    "icons/h5-preview.svg": {
      "url": "/icons/h5-preview.js",
      "type": "js"
    },
    "icons/index.tsx": {
      "url": "/icons/index.js",
      "type": "js",
      "deps": [
        "icons/pc-preview.svg",
        "icons/h5-preview.svg"
      ]
    },
    "editor/DisabledEditorPlugin.tsx": {
      "url": "/editor/DisabledEditorPlugin.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/index"
      ]
    },
    "renderer/MyRenderer.tsx": {
      "url": "/renderer/MyRenderer.js",
      "type": "js"
    },
    "editor/MyRenderer.tsx": {
      "url": "/editor/MyRenderer.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/index"
      ]
    },
    "route/Editor.tsx": {
      "url": "/route/Editor.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor/lib/index",
        "icons/index.tsx",
        "editor/DisabledEditorPlugin.tsx",
        "renderer/MyRenderer.tsx",
        "editor/MyRenderer.tsx"
      ]
    }
  },
  "pkg": {}
});