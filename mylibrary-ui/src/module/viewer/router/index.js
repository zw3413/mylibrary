import pdfviewer from '@/module/viewer/page/pdf.vue'
import txtviewer from '@/module/viewer/page/txt.vue'

export  default [
    {
        path:'/viewer/pdf',
        component:pdfviewer,
        name:'pdfviewer',
        hidden:true
    },
    {
        path:'/viewer/txt',
        component:txtviewer,
        name:'txtviewer',
        hidden:true
    }
]

