# TAK Front

## Installation
- Node Version : 18.20.4 사용
- Node 프로젝트는 다음 명령어들을 통해 언제나 초기화->서버 띄우기 가능하다.
```shell
# Win
Remove-Item -Path ".\node_modules" -Recurse -Force
# Ubuntu
sudo rm -rf node_modules

npm clean cache --force
npm install --legacy-peer-deps # --legacy-peer-deps 는 권장되지 않으나 현재 "ag-grid-*" 라이브러리 들이 react 16.8 를 사용하기 때문에...
npm run start
```

## 소스

### 모듈 구성

- Component 는 다음 구조를 벗어나지 않는다.
  - [1] 설정 -> [2] Recoil -> [3] State -> [4] Refresh -> [5] Hook -> [6] Fetch 함수 -> [7] Event Handler -> [8] LifeCycle -> [9] View
- enableMantineFetch State 는 List API 호출 후 반드시 finally 로 false 로 해줘야 한다.
  - React 는 state 가 대상이 가진 값과 같은 값이 전달 될 경우 무시된다. (다시 말해, 예를 들어 검색 버튼 클릭을 연속 클릭하면 API 가 호출되지 않을 수 있다. 왜냐하면, enableMantineFetch 가 연속으로 두 번 true 로 전달되기 때문이다.)
  - [예시 2] 에서는 List 의 fetch API 가  useMantineFetch 함수 안에서 enableMantineFetch 가 false 로 된다.
- 모든 모듈들은 'routes' 폴더에 있다.
- 전체 공통 모듈은 모두 shared 폴더에 있다.
- ````Local``` 로 끝나는 파일 명들은 recoil 이 아닌 state 를 사용한다. (다시 말해, Recoil 처럼 다른 탭에 갔다오면 정보가 유지되는 것이 아니다.)

- [예시 1]
```javascript
const CarsTestDrivesList = ({recoilKey, ...props}) => {

    /*
    * 
    *   [1] 설정
    * 
    * */
    const isMobile = useMediaQuery('(max-width: 900px)');

    /*
    * 
    *   [2] Global State : Recoil, Redux....
    * 
    * */
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const recoiledResponse = useRecoilValue(boardListResponseSelector({ recoilKey }));
    // 읽기 전용 (달력 검색)
    const calendarDateRange = useRecoilValue(boardListOthersSelector({recoilKey, topic: "calendarDateRange"}));


    // 쓰기 전용
    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));
    const setRecoiledResponse = useSetRecoilState(boardListResponseSelector({ recoilKey }));
    const setCalendarDateRange = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "calendarDateRange"}));

    /// 선택한 One 만 Reset
    const resetOne = useResetRecoilState(boardUpdateResetOneSelector({recoilKey}));
    const resetModifiedOne = useResetRecoilState(boardUpdateResetModifiedOneSelector({recoilKey}));

    // 세션
    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    /// list 초기화 Reset
    const resetList = useRecoilCallback(({set}) => (recoilKey) => {
        set(boardListResetSelector({recoilKey}), null);
    });


    /*
    * 
    *   [3] State
    * 
    * */
    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const columns = React.useMemo(() => getTestDrivesColumns({ PK_NAME}), []);


    /*
    *
    *   [4] Refresh
    *
    * */
    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }

    const refreshAll = () => {
        // 여기 fetch 함수의 전제 조건은 항상 calendarDateRange  에 값이 있어야 하는데, 이렇게 하면 같이 날아감.
        // resetList(recoilKey)
        refreshOne()
        setEnableMantineFetch(true)
    }


    /*
    * 
    *   [5] Hook
    * 
    * */

    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});


    const { secondDepthDepts, selectedSecondDepthDeptIdx, setSelectedSecondDepthDeptIdx
        ,handleSecondDepthDepts } = useSecondDepthDepts({recoilKey});


    /*
    * 
    *   [6] Fetch 함수
    * 
    * */
    const fetchTestDrivesMetasWrapper = useCallback(
        async (controlLoading) => {
            if (controlLoading) {
                setLoading(true);
            }

            if (!calendarDateRange.startDate && !calendarDateRange.endDate) {
                return;
            }

            let re;
            try {
                re = await fetchTestDrivesMetas({
                    skipPagination: true,
                    searchFilter: JSON.stringify({...createSearchFilter(), deptIdx : selectedSecondDepthDeptIdx}),
                    dateRangeFilter: JSON.stringify({
                        column: "startEndDate",
                        startDate: formatDateWrapper(calendarDateRange.startDate),
                        endDate: formatDateWrapper(calendarDateRange.endDate),
                    }),
                });
            } finally {
                setRecoiledResponse(re);
                setEnableMantineFetch(false);
                if (controlLoading) {
                    setLoading(false);
                }
            }
        },
        [selectedSecondDepthDeptIdx, calendarDateRange, createSearchFilter] // 종속성 배열
    );



    /*
    * 
    *   [7] Event Handler (클릭, 선택 등..)
    * 
    * */
    const handleEventClick = useCallback((clickedOne) => {
        refreshOne();
        if (isValidObject(one)) {
            if (clickedOne.original[PK_NAME] !== one[PK_NAME]) {
                setOne({ ...clickedOne });
            } else {
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: false,
                });
            }
        } else {
            setOne({ ...clickedOne });
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true,
            });
        }
    }, [one, refreshOne, setOne, setGlobalSidebarCollapsed]);


    const handleCalendarMonthChange = useCallback(({ startDate, endDate }) => {
        if(startDate && endDate){
            setCalendarDateRange({ startDate, endDate })
        }
    }, []);


    /*
    *   [8] LifeCycle
    * */
    useEffect(()=>{
        handleSecondDepthDepts(true);
    }, [])

    useEffect(()=>{
        if(enableMantineFetch){
            fetchTestDrivesMetasWrapper(true);
        }
    },[enableMantineFetch])

    useEffect(()=>{
        fetchTestDrivesMetasWrapper(true);
    },[calendarDateRange])


    /*
    *   [9] View
    * */    
    return (    <Fragment>
        <LoadingOverlay
            spinner={<ClockLoader color="#ffffff" size={20}/>}
            active={loading}
        >
            <Row>
                <Col xl={12}>
                    <ListSearchModule fields={columns}
                                      searchColumn={searchColumn}
                                      searchValue={searchValue}
                                      onSearchColumnChange={setSearchColumn}
                                      dateRangeColumn={dateRangeColumn}
                                      onDateSearchColumnChange={setDateRangeColumn}
                                      onSearchValueChange={setSearchValue}
                                      dateRange={dateRange} onDateRangeChange={(name, value) => {
                        setSafeDateRange({
                            name, value
                        });
                    }}
                                      onSearchButtonClick={() => {
                                          setEnableMantineFetch(true)
                                      }}
                                      onSearchBoxKeyDown={(e) => {
                                          if (e.keyCode === 13) {
                                              setEnableMantineFetch(true)
                                          }
                                      }} >

                        <Input
                            type="select"
                            id="secondDepthDepts"
                            name="secondDepthDepts"
                            className={"mr-2 mb-2"}
                            value={selectedSecondDepthDeptIdx}
                            defaultValue={""}
                            onChange={(e) => {
                                setSelectedSecondDepthDeptIdx(e.target.value)}}
                            required
                        >
                            {secondDepthDepts.map((option) => {

                                // 표시할 텍스트 결정
                                const displayText = option.deptNm;

                                return (
                                    <option key={option.deptIdx} value={option.deptIdx}>
                                        {displayText}
                                    </option>
                                );
                            })}
                            <option key="-" value=""><b>전시장 전체</b></option>
                        </Input>

                    </ListSearchModule>
                </Col>
            </Row>
            <Row>
                <Col xl={12} >
                    <BigCalendarComponent events={recoiledResponse?.data?.content.map(x => {

                        return {...x,
                            id : x.testDriveIdx,
                            title: `${x.carNo}(${x.deptNm})`,
                            start: x.startDate,
                            end: x.endDate,
                            allDay: false
                        }
                    })} refreshAll={refreshAll} onMonthChange={handleCalendarMonthChange}
                                          onEventClick={handleEventClick} PK_NAME={PK_NAME} loading={loading} setLoading={setLoading} ></BigCalendarComponent>
                </Col>

                <Modal
                    size={"75%"}
                    opened={isCreateOne(one, PK_NAME) || isUpdateOne(one, PK_NAME)}
                    onClose={()=>{
                        refreshOne()
                    }}
                    style={{
                        overflow : "auto"
                    }}
                    zIndex={100000}
                    fullScreen={isMobile}
                    overlayProps={{
                        backgroundOpacity: 0.55,
                        blur: 3,
                    }}
                >
                    {isUpdateOne(one, PK_NAME) ? <TestDrivesUpdate refreshAll={refreshAll}
                                                                   refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                </Modal>

            </Row>

        </LoadingOverlay>
    </Fragment>)
}

```

- [예시 2]

```javascript
const PK_NAME = "customerIdx";
const SettingsCustomersList = ({recoilKey, ...props}) => {

    /*
    *
    *   [1] 설정
    *
    * */
    const isMobile = useMediaQuery('(max-width: 900px)');

    /*
    *   Recoil
    *    : 읽기 - useRecoilValue, 쓰기 - useSetRecoilState, 초기화 - useResetRecoilState
    * */

    /*
    *
    *   [2] Global State : Recoil, Redux....
    *
    * */
    // 읽기 전용
    const one = useRecoilValue(boardUpdateOneSelector({recoilKey}));
    const deptsWithUsersListOthers = useRecoilValue(boardListOthersSelector({recoilKey, topic: "deptsWithUsers"}));
    const deptWithUserListOthers = useRecoilValue(boardListOthersSelector({
        recoilKey,
        topic: "selectedDeptWithUser"
    }));
    const codeCustomersListOthers = useRecoilValue(boardListOthersSelector({recoilKey, topic: "codeCustomers"}));
    const selectedRowsOthers = useRecoilValue(boardListOthersSelector({
        recoilKey,
        topic: "selectedRows"
    }));

    // 쓰기 전용
    const setGlobalSidebarCollapsed = useSetRecoilState(globalInfoSidebarCollapsedSelector());

    const setOne = useSetRecoilState(boardUpdateOneSelector({recoilKey}));
    const setDeptsWithUsersListOthers = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "deptsWithUsers"}));
    const setSelectedDeptWithUserListOthers = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedDeptWithUser"
    }));
    const setCodeCustomersListOthers = useSetRecoilState(boardListOthersSelector({recoilKey, topic: "codeCustomers"}));
    const setSelectedRowsOthers = useSetRecoilState(boardListOthersSelector({
        recoilKey,
        topic: "selectedRows"
    }));
    /// 선택한 One 만 Reset
    const resetOne = useResetRecoilState(boardUpdateResetOneSelector({recoilKey}));
    const resetModifiedOne = useResetRecoilState(boardUpdateResetModifiedOneSelector({recoilKey}));
    // 세션
    const me = useRecoilValue(globalInfoAccessTokenUserInfoSelector());

    /// list 초기화 Reset
    const resetList = useRecoilCallback(({set}) => (recoilKey) => {
        set(boardListResetSelector({recoilKey}), null);
    });

    /*
    *
    *   [3] State
    *
    * */
    const [loading, setLoading] = useState(false);
    const [enableMantineFetch, setEnableMantineFetch] = useState(null);
    const [mantineFetchFirst, setMantineFetchFirst] = useState(true);

    const [usersListSelectModalOpen, setUsersListSelectModalOpen] = useState(false);

    const columns = React.useMemo(() => getCustomersListColumns({codeCustomersListOthers, PK_NAME}), [codeCustomersListOthers]);

    const pageSideData = (() => {
        const isRelocateTo = recoilKey === 'CustomersRelocateTo' && props.location?.pathname?.split('/').at(-1) === 'relocateto';
        if(isRelocateTo) {
            return {
                toolbarConfig: {createBtn: false},
                searchFilterConfig: {userOutYn: "Y"}
            }
        }
    })();


    /*
    *
    *   [4] Refresh
    *
    * */
    const refreshOne = () => {
        resetOne()
        resetModifiedOne()
    }
    const refreshAll = async () => {
        resetList(recoilKey)
        // resetList 물론 rowSelection 정보가 날아가지만 현재 아래에 useMantineTable 에서 state 에 rowSelection 을 mantineMeta 를 통해 관리하고 있지 않으므로, 동기화 필요.
        refreshOne()
        listPrevCycle(true)
        setEnableMantineFetch(true)
    }

    /*
    *
    *   [5] Hook
    *
    * */

    /*
    *
    *  Mantine 의 검색 조건 (Meta) 을 담당
    *    : columnFilters, columnFilterFns, globalFilter : 더 이상 미사용
    *    : sorting, pagination : 사용 중
    * */
    const {
        columnFilters, setColumnFilters,
        columnFilterFns, setColumnFilterFns,
        globalFilter, setGlobalFilter,
        sorting, setSorting,
        pagination, setPagination
    } = useMantineMeta({columns, recoilKey});

    /*
    *   Mantine 의 검색 조건 (Meta) 을 담당 (그런데, Mantine 에서 제공하는 것이 아닌, <ListSearchModule/> 을 사용)
    *    : UI 를 보시면 ( dateRangeColumn , dateRange, searchColumn, searchValue )
    * */
    const {
        createSearchFilter, createDateRangeFilter,
        searchColumn, setSearchColumn, searchValue, setSearchValue,
        dateRangeColumn, setDateRangeColumn,
        dateRange, setSafeDateRange
    } = useListSearchModuleMeta({recoilKey});

    /*
    *
    *   실제 DB 가져오기
    *
    * */
    const {data, error, isError, isFetching, isLoading} = useMantineFetch({
        columnFilterFns,
        columnFilters,
        globalFilter,
        pagination,
        sorting,
        fetchFunc: agent.Customer.fetch,
        cacheKey: recoilKey,
        additionalSearchFilter: {...createSearchFilter(), deptWithUser : deptWithUserListOthers, ...pageSideData?.searchFilterConfig ?? null },
        additionalDateRangeFilter: createDateRangeFilter(),
        enabled: enableMantineFetch,
        setEnabled: setEnableMantineFetch,
        isFirstRender: mantineFetchFirst,
        setIsFirstRender: setMantineFetchFirst
    });

    const table = useMantineReactTable({
        columns,
        enableColumnResizing: true,
        columnResizeMode: 'onEnd',
        /*        mantinePaperProps: {
                    style: { '--mrt-base-background-color': 'rgb(33, 24, 44)' },
                },*/
        enableStickyHeader: true,
        mantinePaginationProps: {
            radius: 'xl',
            size: 'md',
        },
        mantineTableProps: {
            striped: true,
            verticalSpacing: "xs",
            sx: {

                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTableBodyProps: {},
        mantineTableHeadProps: {
            sx: {
                //    zIndex: 1.99
            }
        },
        mantineTableHeadCellProps: {
            sx: {
                fontFamily: 'Verdana, sans-serif',
                fontFamilyMonospace: 'Monaco, Courier, monospace'
            },
        },
        mantineTopToolbarProps :{
            sx: {
            }
        },
        mantineTableFooterProps: {},
        data: data?.data?.content ?? [],
        initialState: {showColumnFilters: false, density: 'xs', showGlobalFilter: false},
        // manual~ 의 true 에 아래 on 함수 하나씩 필요
        manualPagination: true,
        manualSorting: true,
        onPaginationChange: (funcOrObject) => {
            setEnableMantineFetch(true)
            setPagination(funcOrObject)
        },
        onSortingChange: (funcOrArray) => {
            setEnableMantineFetch(true)
            setSorting(funcOrArray)
        },
        mantineToolbarAlertBannerProps: isError
            ? {
                color: 'red',
                children: error?.userMessage,
            }
            : undefined,
        rowCount: data?.data?.totalElements ?? 0,
        state: {
            pagination,
            sorting,
            // rowSelection : selectedRowsOthers,
            isLoading,
            showAlertBanner: isError,
        },
        renderTopToolbar: ({table}) => {

            return (
                <Fragment>
                    <Row>
                        <Col className={"ml-4"}>
                            <ListTopToolbar
                                table={table}
                                recoilKey={recoilKey}
                                refreshAll={refreshAll}
                                columns={columns}
                                searchColumn={searchColumn}
                                setSearchColumn={setSearchColumn}
                                searchValue={searchValue}
                                setSearchValue={setSearchValue}
                                dateRange={dateRange}
                                setSafeDateRange={setSafeDateRange}
                                dateRangeColumn={dateRangeColumn}
                                setDateRangeColumn={setDateRangeColumn}
                                createBtn={pageSideData?.toolbarConfig?.createBtn ?? true}
                                createOne={handleCreateOneClick}
                                setEnableMantineFetch={setEnableMantineFetch}
                                searchModuleShow={recoilKey !== 'CustomersRelocateTo'}
                            >
                                <Input
                                    style={{ width : "300px"}}
                                    type="select"
                                    id="deptsWithUsers"
                                    name="deptsWithUsers"
                                    className={"mr-2 mb-2"}
                                    value={deptWithUserListOthers.compositeValue}
                                    defaultValue={""}
                                    onChange={(e) => setSelectedDeptWithUserListOthers(e.target.value)}
                                    required
                                >
                                    {deptsWithUsersListOthers.map((option) => {
                                        const value = JSON.stringify({
                                            deptIdx: option.deptIdx,
                                            deptNm: option.deptNm,
                                            userIdx: option.userIdx,
                                            userId : option.userId,
                                            userName: option.userName,
                                            depth: option.depth
                                        });


                                        // depth 만큼 공백 추가
                                        const spaces = '\u00A0'.repeat(option.depth * 2); // 각 depth에 대해 4칸 공백 추가

                                        // 표시할 텍스트 결정
                                        const displayText = !option.userIdx ? option.deptNm : option.userName;

                                        return (
                                            <option key={option.deptIdx + "-" + option.userIdx} value={value}>
                                                {spaces}[{option.depth}] {displayText}
                                            </option>
                                        );
                                    })}
                                    <option key="-" value="">담당자와 그의 조직으로 검색</option>
                                </Input>

                            </ListTopToolbar>

                        </Col>
                    </Row>
                    <Row>
                        <Col lg={7} className={"d-flex align-items-center ml-4 mb-4"}>
                            <Button
                                className={classNames('ml-2')}
                                leftIcon={<IconDeviceMobileMessage size={14}/>}
                                variant="gradient"
                                gradient={{ from: 'orange', to: 'red' }}
                                /*disabled={!(table.getIsSomeRowsSelected() || table.getIsAllRowsSelected())}*/
                                onClick={handleSMSSendAction}
                            >
                                SMS전송
                            </Button>
                            <Button
                                className={classNames('ml-2')}
                                leftIcon={<IconMoodSearch size={14}/>}
                                variant="gradient"
                                gradient={{ from: 'orange', to: 'red' }}
                                /*disabled={!(table.getIsSomeRowsSelected() || table.getIsAllRowsSelected())}*/
                                onClick={handleUserChangeAction}
                            >
                                담당사원변경
                            </Button>
                        </Col>
                    </Row>
                </Fragment>
            );
        },
        /*       renderDetailPanel: ({ row }) => {
                   return (<div className={"w-50"}><MenusUserEdit one={data?.data?.content.find(x => x[PK_NAME] === row?.original[PK_NAME])} refetch={refetch}/></div>)
               },*/
        mantineTableContainerProps : ({row}) => ({
            /*            onClick: (e) => {
                            alert('aaa')
                        },*/
            sx: {cursor: 'pointer', maxHeight  :"500px"},
        }),
        mantineTableBodyRowProps: ({row}) => ({
            onClick: (e) => {
                handleRowClick({row})
            },
            sx: {cursor: 'pointer'},
        }),
        //   enableRowActions: true,
        localization: {...MRT_Localization_KO_CUSTOM, actions: "수정"},
        paginationDisplayMode: 'pages',
        renderBottomToolbar: ({table}) => {
            return (<Fragment>
                <Row>
                    <Col lg={12} className={"d-flex align-items-center ml-4 mt-1"}>
                        <MRT_TablePagination table={table}  />
                    </Col>
                </Row>
            </Fragment>)
        },
        enableRowSelection: true,
        /*        onRowSelectionChange : () =>{

                },*/
        mantineSelectCheckboxProps: {
            color: 'orange',
        },
        enableMultiSort : false
        // positionPagination : "top"

    });


    /*
    *
    *   [6] Fetch 함수
    *
    * */
    const listPrevCycle = (always) => {
        // 조직 + 담당자 검색 기능
        setDeptsWithUsers(always);
        // Code 테이블 의존 Select box : (예) 접촉 경로
        setCodeCustomersMetas(always);
    }

    /*
    *   셀렉트 박스 : 접촉 경로
    * */
    const setCodeCustomersMetas = async (always = false) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        if(codeCustomersListOthers.length === 0 || always) {
            const re = await Promise.all([fetchCodeCustomersMetas()]);
            setCodeCustomersListOthers(re[0])
        }
    }

    /*
    *   특수 검색 : "담당 조직 또는 사원으로 검색"
    * */
    const setDeptsWithUsers = async (always = false) => {
        if(deptsWithUsersListOthers.length === 0 || always) {
            const deptsWithUsers = await getDeptsWithUsers();
            setDeptsWithUsersListOthers(deptsWithUsers)
        }
    }
    const getDeptsWithUsers = async () => {
        const re = await Promise.all([fetchDeptsForCurrentDealer(), fetchUsersForCurrentDealer()]);

        if (re[0] && re[1]) {
            // depts
            const depts = sortDeptsByParentCd(re[0].map(x => ({
                deptIdx: x.deptIdx,
                parentCd: x.parentCd,
                deptNm: x.deptNm,
                depth: DeptHierarchyUtil.getDepthByDeptIdx(re[0], x.deptIdx)
            })));
            // users
            const users = re[1];

            return DeptHierarchyUtil.mergeDeptsAndUsers(depts, users);
        } else {
            return [];
        }
    };

    /*
     *    Fetch Cycle : 1개 단위
     * */
    const onePrevCycle = async ({localOne}) => {
        // 이번에 클릭한 row 의 객체로 넣는다.
        setLoading(true)
        try {
            const re = await Promise.all([fetchCodeCustomersMetas(), fetchCustomerGroupsMetas(), fetchPrivacyAgree(localOne.customerIdx)]);
            setOne({
                ...localOne, meta: {
                    codeCustomers: re[0] ? re[0] : [],
                    customerGroups : re[1] ? re[1] : [],
                    privacyAgree : re[2] ? re[2] : [],
                }
            });
            setGlobalSidebarCollapsed({
                forceUpdate: Math.random(),
                value: true
            })
        } finally {
            setLoading(false)
        }
    }


    /*
    *
    *   [7] Event Handler (클릭, 선택 등..)
    *
    * */
    const handleSMSSendAction = () => {
        if(table.getSelectedRowModel().rows.length < 1) {
            alert('선택된 고객이 없습니다.');
            return;
        }

        alert('개발 준비 중');
        table.getSelectedRowModel().flatRows.map((row) => {
            console.log(row);
        });
    };

    const handleUserChangeAction = () => {
        if(table.getSelectedRowModel().rows.length < 1) {
            alert('선택된 고객이 없습니다.');
            return;
        }
        setUsersListSelectModalOpen(true)
    };

    const handleUserSelected = (originalUserRow) => {
        // 선택된 행의 고객 이름을 문자열로 합치기
        const customersStr = table.getSelectedRowModel().rows.map(x => x.original.customerName).join(',');

        // 확인 메시지 구성
        const confirmationMessage =
            `${originalUserRow.dealerNm} (${originalUserRow.deptNm}) 소속의 ${originalUserRow.name} 직원에게 ${customersStr} 고객을 담당하게 하시겠습니까?`;

        // 사용자 확인
        if (confirm(confirmationMessage)) {
            updateCustomersUserManager({ customerIdxList : table.getSelectedRowModel().
                rows.map(x => x.original.customerIdx), userIdx : originalUserRow.userIdx, refreshAll });

            setUsersListSelectModalOpen(false);
        }
    }

    // 신규 버튼 클릭 시
    const handleCreateOneClick = () => {
        refreshOne()
        onePrevCycle({localOne: {[PK_NAME]: null}})
    }

    // 행 클릭 시
    const handleRowClick = async ({row}) => {

        refreshOne()
        // console.log('수정버튼클릭')
        // console.log(row)
        // console.log(table)
        if (isValidObject(one)) {
            // row.original[PK_NAME] : 현재 수정 버튼을 클릭한 row 의 PK 값
            // one[PK_NAME] : 이전에 수정 버튼을 클릭하여 넣어진 recoil 의 PK 값
            if (row.original[PK_NAME] !== one[PK_NAME]) {
                // console.log("1 : 이전에 클릭한 것과 다른 row 를 클릭하였다.")

                onePrevCycle({localOne: row.original})
            } else {
                //  console.log("2 : 이전에 클릭한 것과 같은 row 를 클릭하였다.")
                // 중앙 저장소(recoil)의 현재 RECOIL_KEY에 해당하는 one 을 초기화 (위에서 초기화 하였음)
                // refreshOne()
                setGlobalSidebarCollapsed({
                    forceUpdate: Math.random(),
                    value: false
                })
            }
        } else {
            // console.log("3  : one 이 isValidObject 아니라는 의미는, 한번도 수정 버튼을 클릭한 적이 없다.")

            onePrevCycle({localOne: row.original})
        }
    }



    /*
    *   [8] LifeCycle
    * */

    // 여기는 1) 최초 진입하거나 2) 다른 탭에 갔다가 오는 경우 (이 또한 recoil 이 유지 될 뿐 최초 진입이나 다름 없음)
    useEffect(() => {
        setMantineFetchFirst(true)
        setEnableMantineFetch(true)

        listPrevCycle(false)
    }, [])


    // DB 재조회를 위한 신호가 켜지면 선택된 select box 들 모두 해제
    useEffect(()=>{
        if(enableMantineFetch) {
            table.resetRowSelection();
        }
    },[enableMantineFetch])

    // 여기는 1) 최초 진입하거나 2) 다른 탭에 갔다가 오는 경우 (이 또한 recoil 이 유지 될 뿐 최초 진입이나 다름 없음)
    // 아래 table 객체는 위 1),2) 경우를 제외하고는 table 객체 내 다른 상태가 변한다고 해서 감지되지 않음을 확인.
    useEffect(() => {

        // 이 것을 안해주면 예를 들어 2페이지에 있다가 다른 탭에 갔다오면 1페이지로 초기화 되는 버그가 있다.
        // 이는 Mantine table 이 참조하는 Tanstack 테이블 문제로 보인다.
        // https://www.mantine-react-table.com/docs/api/table-options#onPaginationChange-prop
        // pagination recoil 은 문제가 없다. 그렇다고, const table 자체를 모두 recoil 저장 하려면 안에 수 많은 함수들과 참조 때문에 저장 실패한다.
        // 초기화 되는 문제가 있는 다른 것들이 발견된다면 여기서 set 한다.
        table.setPagination(pagination)
        // console.log("aaa")
        // console.log(selectedRowsOthers)
        table.setRowSelection(selectedRowsOthers);

    }, [table])

    useEffect(() => {
        // console.log("bbb")
        // console.log(table.getState())
        // console.log(table.getSelectedRowModel().rows)
        setSelectedRowsOthers(table.getState().rowSelection)
    }, [table.getState().rowSelection])




    /*
    *  클래스 css 사용법 : https://www.w3schools.com/bootstrap4/bootstrap_grid_xlarge.asp
    *
    *  Row = <div className="row">
       Col = <div className="col-lg-4">
    * */
    return (
        <Fragment>
            <UsersListSelectModal onUserSelected={handleUserSelected} usersListSelectModalOpen={usersListSelectModalOpen} setUsersListSelectModalOpen={setUsersListSelectModalOpen}/>
            <LoadingOverlay
                spinner={<ClockLoader color="#ffffff" size={20}/>}
                active={loading}
            >
                <Row>
                    <Col xl={12} >
                        <MantineReactTable table={table}/>
                    </Col>

                    <Modal
                        size={"75%"}
                        opened={isCreateOne(one, PK_NAME) || isUpdateOne(one, PK_NAME)}
                        onClose={()=>{
                            refreshOne()
                        }}
                        style={{
                            overflow : "auto"
                        }}
                        zIndex={100000}
                        fullScreen={isMobile}
                        overlayProps={{
                            backgroundOpacity: 0.55,
                            blur: 3,
                        }}
                    >
                        {isCreateOne(one, PK_NAME) ? <SettingsCustomersCreate refreshAll={refreshAll} refreshOne={refreshOne}
                                                                              recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                        {isUpdateOne(one, PK_NAME) ? <SettingsCustomersUpdate createOne={handleCreateOneClick} refreshAll={refreshAll}
                                                                              refreshOne={refreshOne} recoilKey={recoilKey} PK_NAME={PK_NAME}/> : ""}
                    </Modal>

                </Row>

            </LoadingOverlay>
        </Fragment>)
};

```
