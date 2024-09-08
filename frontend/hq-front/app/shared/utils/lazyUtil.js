import { lazy } from 'react'

export const retryLazy = (componentImport) =>
    lazy(async () => {
        const pageAlreadyRefreshed = JSON.parse(
            window.localStorage.getItem('pageRefreshed') || 'false'
        )
        try {
            const component = await componentImport()
            window.localStorage.setItem('pageRefreshed', 'false')
            return component
        } catch (error) {
            if (!pageAlreadyRefreshed) {
                window.localStorage.setItem('pageRefreshed', 'true')
                return window.location.reload()
            }
            throw error
        }
    })
