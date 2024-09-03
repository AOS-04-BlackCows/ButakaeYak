Pill) Result Code & Message
00  SUCCESS                                             성공
1	APPLICATION_ERROR	                                어플리케이션 에러
4	HTTP ERROR	HTTP                                    에러
10	INVALID_REQUEST_PARAMETER_ERROR	                    잘못된 요청 파라메터 에러
12	NO_OPENAPI_SERVICE_ERROR	                        해당 오픈API서비스가 없거나 폐기됨
20	SERVICE_ACCESS_DENIED_ERROR	                        서비스 접근거부
22	LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR	서비스 요청제한횟수 초과에러
30	SERVICE_KEY_IS_NOT_REGISTERED_ERROR	                등록되지 않은 서비스키
31	DEADLINE_HAS_EXPIRED_ERROR	                        활용기간 만료
32	UNREGISTERED_IP_ERROR	                            등록되지 않은 IP
99	UNKNOWN_ERROR	                                    기타에러


Drug) Result Code & Message
00  SUCCESS                                             성공
01  Application Error                                   서비스 제공 상태가 원할하지 않습니다.
02  DB Error                                            서비스 제공 상태가 원할하지 않습니다.
03  No Data                                             데이터 없음
04  Http Error                                          
05  Service Time Out                                    
10  잘못된 요청 파라미터 에러
11  필수 요청 파라미터 없음
12  해당 서비스가 없거나 폐기됨
20  서비스 접근 거부
22  해당 서비스 요청 제한 횟수 초과
30  등록되지 않은 서비스키
31  기한 만료된 서비스키
32  등록되지 않은 Ip 및 도메인명