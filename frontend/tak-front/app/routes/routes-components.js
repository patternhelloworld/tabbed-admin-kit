import React from 'react';
import { retryLazy } from '../shared/utils/lazyUtil.js';

import Analytics from './Dashboards/Analytics';
import ProjectsDashboard from './Dashboards/Projects';
import System from './Dashboards/System';
import Monitor from './Dashboards/Monitor';
import Financial from './Dashboards/Financial';
import Stock from './Dashboards/Stock';
import Reports from './Dashboards/Reports';

import Widgets from './Samples/Widgets';

import Cards from './Samples/Cards/Cards';
import CardsHeaders from './Samples/Cards/CardsHeaders';

import NavbarOnly from '../layout/components/NavbarOnly';
import SidebarDefault from '../layout/components/SidebarDefault';
import SidebarA from '../layout/components/SidebarA';
import DragAndDropLayout from '../layout/components/DragAndDropLayout';
import SidebarWithNavbar from '../layout/components/SidebarWithNavbar';

import Accordions from './Samples/Interface/Accordions';
import Alerts from './Samples/Interface/Alerts';
import Avatars from './Samples/Interface/Avatars';
import BadgesLabels from './Samples/Interface/BadgesLabels';
import Breadcrumbs from './Samples/Interface/Breadcrumbs';
import Buttons from './Samples/Interface/Buttons';
import Colors from './Samples/Interface/Colors';
import Dropdowns from './Samples/Interface/Dropdowns';
import Images from './Samples/Interface/Images';
import ListGroups from './Samples/Interface/ListGroups';
import MediaObjects from './Samples/Interface/MediaObjects';
import Modals from './Samples/Interface/Modals';
import Navbars from './Samples/Interface/Navbars';
import Paginations from './Samples/Interface/Paginations';
import ProgressBars from './Samples/Interface/ProgressBars';
import TabsPills from './Samples/Interface/TabsPills';
import TooltipPopovers from './Samples/Interface/TooltipsPopovers';
import Typography from './Samples/Interface/Typography';
import Notifications from './Samples/Interface/Notifications';
import CropImage from './Samples/Interface/CropImage';
import DragAndDropElements from './Samples/Interface/DragAndDropElements';
import Calendar from './Samples/Interface/Calendar';
import ReCharts from './Samples/Graphs/ReCharts';

import Forms from './Samples/Forms/Forms';
import FormsLayouts from './Samples/Forms/FormsLayouts';
import InputGroups from './Samples/Forms/InputGroups';
import Wizard from './Samples/Forms/Wizard';
import TextMask from './Samples/Forms/TextMask';
import Typeahead from './Samples/Forms/Typeahead';
import Toggles from './Samples/Forms/Toggles';
import Editor from './Samples/Forms/Editor';
import DatePicker from './Samples/Forms/DatePicker';
import Dropzone from './Samples/Forms/Dropzone';
import Sliders from './Samples/Forms/Sliders';


import AccountEdit from './Samples/Apps/AccountEdit';
import BillingEdit from './Samples/Apps/BillingEdit';
import Chat from './Samples/Apps/Chat';
import Clients from './Samples/Apps/Clients';
import EmailDetails from './Samples/Apps/EmailDetails';
import Files from './Samples/Apps/Files';
import GalleryGrid from './Samples/Apps/GalleryGrid';
import GalleryTable from './Samples/Apps/GalleryTable';
import ImagesResults from './Samples/Apps/ImagesResults';
import Inbox from './Samples/Apps/Inbox';
import NewEmail from './Samples/Apps/NewEmail';
import ProfileDetails from './Samples/Apps/ProfileDetails';
import ProfileEdit from './Samples/Apps/ProfileEdit';
import Projects from './Samples/Apps/Projects';
import SearchResults from './Samples/Apps/SearchResults';
import SessionsEdit from './Samples/Apps/SessionsEdit';
import SettingsEdit from './Samples/Apps/SettingsEdit';
import Tasks from './Samples/Apps/Tasks';
import TasksDetails from './Samples/Apps/TasksDetails';
import TasksKanban from './Samples/Apps/TasksKanban';
import Users from './Samples/Apps/Users';
import UsersResults from './Samples/Apps/UsersResults';
import VideosResults from './Samples/Apps/VideosResults';

import ComingSoon from './Samples/Pages/ComingSoon';
import Confirmation from './Samples/Pages/Confirmation';
import Danger from './Samples/Pages/Danger';
import Error404 from './Samples/Pages/Error404';
import ForgotPassword from './Samples/Pages/ForgotPassword';
import LockScreen from './Samples/Pages/LockScreen';

const Login = retryLazy(() => import('./Pages/Login'));
import Register from './Samples/Pages/Register';
import Success from './Samples/Pages/Success';
import Timeline from './Samples/Pages/Timeline';

import Icons from './Samples/Icons';

const SamplesMantine = retryLazy(() => import('./Samples/Mantine'));

const SettingsMenusMains = retryLazy(() => import('./Settings/Menus/Mains'));
const SettingsMenusSubs = retryLazy(() => import('./Settings/Menus/Subs'));
const SettingsDepts = retryLazy(() => import('./Settings/Depts'));
const SettingsDealers = retryLazy(() => import('./Settings/Dealers'));
const SettingsUsersMenus = retryLazy(() => import('./Settings/Users/Menus'));
const SettingsUsers = retryLazy(() => import('./Settings/Users'));
const SettingsApprovalLines = retryLazy(() => import('./Settings/ApprovalLines'));

const CodesCustomer = retryLazy(() => import('./Codes/Customer'));
const CustomersList = retryLazy(() => import('./Customers/List'));
const CustomersGroups = retryLazy(() => import('./Customers/Groups'));
const CustomersGroupAssign = retryLazy(() => import('./Customers/GroupAssign'));

const CustomersRelocateTo = retryLazy(() => import('./Customers/List'));
const CustomersRelocatedFrom = retryLazy(() => import('./Customers/RelocatedFrom'));

const CustomersAfterDelivery = retryLazy(() => import('./Customers/List'));
const CustomersExpectationsPurchases = retryLazy(() => import('./Customers/Expectations/Purchases'));

const CodesExtColors = retryLazy(() => import('./Codes/ExtColor'));
const CodesIntColors = retryLazy(() => import('./Codes/IntColor'));


const CarsDealerStocks = retryLazy(() => import('./Cars/DealerStocks'));
const CarsVins = retryLazy(() => import('./Cars/Vins'));
const CarsTestDrives = retryLazy(() => import('./Cars/TestDrives'));

//const TextEditors = retryLazy(() => import('./components/editors/text-editors/TextEditors'));
const anonymousRoutesComponents = [
    { recoilKey : 'Login', component: Login },


    { path: '/dashboards/analytics', exact: true, name: 'Analytics', component: Analytics },

    { path: '/dashboards/system', exact: true, name: 'System', component: System },
    { path: '/dashboards/monitor', exact: true, name: 'Monitor', component: Monitor },
    { path: '/dashboards/financial', exact: true, name: 'Financial', component: Financial },
    { path: '/dashboards/stock', exact: true, name: 'Stock', component: Stock },
    { path: '/dashboards/reports', exact: true, name: 'Reports', component: Reports },

    { path: '/widgets', exact: true, name: 'Widgets', component: Widgets },

    { path: '/cards/cards', exact: true, name: 'Cards', component: Cards },
    { path: '/cards/cardsheaders', exact: true, name: 'CardsHeaders', component: CardsHeaders },

    { path: '/layouts/navbar', exact: true, name: 'NavbarOnly', component: NavbarOnly },
    { path: '/layouts/sidebar', exact: true, name: 'SidebarDefault', component: SidebarDefault },
    { path: '/layouts/sidebar-a', exact: true, name: 'SidebarA', component: SidebarA },
    { path: '/layouts/sidebar-with-navbar', exact: true, name: 'SidebarWithNavbar', component: SidebarWithNavbar },
    { path: '/layouts/dnd-layout', exact: true, name: 'DragAndDropLayout', component: DragAndDropLayout },

    { path: '/interface/accordions', exact: true, name: 'Accordions', component: Accordions },
    { path: '/interface/alerts', exact: true, name: 'Alerts', component: Alerts },
    { path: '/interface/avatars', exact: true, name: 'Avatars', component: Avatars },
    { path: '/interface/badges-and-labels', exact: true, name: 'BadgesLabels', component: BadgesLabels },
    { path: '/interface/breadcrumbs', exact: true, name: 'Breadcrumbs', component: Breadcrumbs },
    { path: '/interface/buttons', exact: true, name: 'Buttons', component: Buttons },
    { path: '/interface/colors', exact: true, name: 'Colors', component: Colors },
    { path: '/interface/dropdowns', exact: true, name: 'Dropdowns', component: Dropdowns },
    { path: '/interface/images', exact: true, name: 'Images', component: Images },
    { path: '/interface/list-groups', exact: true, name: 'ListGroups', component: ListGroups },
    { path: '/interface/media-objects', exact: true, name: 'MediaObjects', component: MediaObjects },
    { path: '/interface/modals', exact: true, name: 'Modals', component: Modals },
    { path: '/interface/navbars', exact: true, name: 'Navbars', component: Navbars },
    { path: '/interface/paginations', exact: true, name: 'Paginations', component: Paginations },
    { path: '/interface/progress-bars', exact: true, name: 'ProgressBars', component: ProgressBars },
    { path: '/interface/tabs-pills', exact: true, name: 'TabsPills', component: TabsPills },
    { path: '/interface/tooltips-and-popovers', exact: true, name: 'TooltipPopovers', component: TooltipPopovers },
    { path: '/interface/typography', exact: true, name: 'Typography', component: Typography },
    { path: '/interface/notifications', exact: true, name: 'Notifications', component: Notifications },
    { path: '/interface/crop-image', exact: true, name: 'CropImage', component: CropImage },
    { path: '/interface/drag-and-drop-elements', exact: true, name: 'DragAndDropElements', component: DragAndDropElements },
    { path: '/interface/calendar', exact: true, name: 'Calendar', component: Calendar },

    { path: '/forms/forms', exact: true, name: 'Forms', component: Forms },
    { path: '/forms/forms-layouts', exact: true, name: 'FormsLayouts', component: FormsLayouts },
    { path: '/forms/input-groups', exact: true, name: 'InputGroups', component: InputGroups },
    { path: '/forms/wizard', exact: true, name: 'Wizard', component: Wizard },
    { path: '/forms/text-mask', exact: true, name: 'TextMask', component: TextMask },
    { path: '/forms/typeahead', exact: true, name: 'Typeahead', component: Typeahead },
    { path: '/forms/toggles', exact: true, name: 'Toggles', component: Toggles },
    { path: '/forms/editor', exact: true, name: 'Editor', component: Editor },
    { path: '/forms/date-picker', exact: true, name: 'DatePicker', component: DatePicker },
    { path: '/forms/dropzone', exact: true, name: 'Dropzone', component: Dropzone },
    { path: '/forms/sliders', exact: true, name: 'Sliders', component: Sliders },

    { path: '/graphs/re-charts', exact: true, name: 'ReCharts', component: ReCharts },

    { path: '/apps/account-edit', exact: true, name: 'AccountEdit', component: AccountEdit },
    { path: '/apps/billing-edit', exact: true, name: 'BillingEdit', component: BillingEdit },
    { path: '/apps/chat', exact: true, name: 'Chat', component: Chat },
    { path: '/apps/clients', exact: true, name: 'Clients', component: Clients },
    { path: '/apps/email-details', exact: true, name: 'EmailDetails', component: EmailDetails },
    { path: '/apps/files/:type', exact: true, name: 'Files', component: Files },
    { path: '/apps/gallery-grid', exact: true, name: 'GalleryGrid', component: GalleryGrid },
    { path: '/apps/gallery-table', exact: true, name: 'GalleryTable', component: GalleryTable },
    { path: '/apps/images-results', exact: true, name: 'ImagesResults', component: ImagesResults },
    { path: '/apps/inbox', exact: true, name: 'Inbox', component: Inbox },
    { path: '/apps/new-email', exact: true, name: 'NewEmail', component: NewEmail },
    { path: '/apps/profile-details', exact: true, name: 'ProfileDetails', component: ProfileDetails },
    { path: '/apps/profile-edit', exact: true, name: 'ProfileEdit', component: ProfileEdit },
    { path: '/apps/projects/:type', exact: true, name: 'Projects', component: Projects },
    { path: '/apps/search-results', exact: true, name: 'SearchResults', component: SearchResults },
    { path: '/apps/sessions-edit', exact: true, name: 'SessionsEdit', component: SessionsEdit },
    { path: '/apps/settings-edit', exact: true, name: 'SettingsEdit', component: SettingsEdit },
    { path: '/apps/tasks/:type', exact: true, name: 'Tasks', component: Tasks },
    { path: '/apps/task-details', exact: true, name: 'TasksDetails', component: TasksDetails },
    { path: '/apps/tasks-kanban', exact: true, name: 'TasksKanban', component: TasksKanban },
    { path: '/apps/users/:type', exact: true, name: 'Users', component: Users },
    { path: '/apps/users-results', exact: true, name: 'UsersResults', component: UsersResults },
    { path: '/apps/videos-results', exact: true, name: 'VideosResults', component: VideosResults },

    { path: '/pages/coming-soon', exact: true, name: 'ComingSoon', component: ComingSoon },
    { path: '/pages/confirmation', exact: true, name: 'Confirmation', component: Confirmation },
    { path: '/pages/danger', exact: true, name: 'Danger', component: Danger },
    { path: '/pages/forgot-password', exact: true, name: 'ForgotPassword', component: ForgotPassword },
    { path: '/pages/lock-screen', exact: true, name: 'LockScreen', component: LockScreen },

    { path: '/pages/register', exact: true, name: 'Register', component: Register },
    { path: '/pages/success', exact: true, name: 'Success', component: Success },
    { path: '/pages/timeline', exact: true, name: 'Timeline', component: Timeline },

    { path: '/icons', exact: true, name: 'Icons', component: Icons}
]

const authenticatedRoutesComponents = [

    { recoilKey : 'ProjectsDashboard', component: ProjectsDashboard },
    { recoilKey : 'Issues',  component: SamplesMantine },

    { recoilKey : 'CarsTestDrives', component : CarsTestDrives},
    { recoilKey : 'CarsDealerStocks', component: CarsDealerStocks},
    { recoilKey : 'CarsVins', component: CarsVins},

    { recoilKey : 'SettingsMenusMains', component: SettingsMenusMains },
    { recoilKey : 'SettingsMenusSubs', component: SettingsMenusSubs },
    { recoilKey : 'SettingsDealers', component: SettingsDealers },
    { recoilKey : 'SettingsDepts',  component: SettingsDepts },
    { recoilKey : 'SettingsUsersMenus', component: SettingsUsersMenus },
    { recoilKey : 'SettingsUsers', component: SettingsUsers },
    { recoilKey : 'SettingsApprovalLines', component: SettingsApprovalLines },

    { recoilKey : 'CodesCustomer', component: CodesCustomer },
    { recoilKey : 'CodesExtColors', component: CodesExtColors},
    { recoilKey : 'CodesIntColors', component: CodesIntColors},

    { recoilKey : 'CustomersExpectationsPurchases', component: CustomersExpectationsPurchases },

    { recoilKey : 'CustomersList', component: CustomersList },
    { recoilKey : 'CustomersGroups', component: CustomersGroups },
    { recoilKey : 'CustomersGroupAssign', component: CustomersGroupAssign },

    { recoilKey : 'CustomersRelocateTo', component: CustomersRelocateTo},
    { recoilKey : 'CustomersRelocatedFrom', component: CustomersRelocatedFrom},

    { recoilKey : 'CustomersAfterDelivery', component: CustomersAfterDelivery},




]
export { anonymousRoutesComponents, authenticatedRoutesComponents };
