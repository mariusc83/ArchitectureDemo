package org.mariusconstantin.dashlanetest.data;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusconstantin.dashlanetest.BuildConfig;
import org.mariusconstantin.dashlanetest.IAppConfig;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.data.models.WebsiteModel;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Marius on 1/1/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class DataProviderTest {
    private final String mValidTestData = "000webhost_com.png\r\n" +
            "000webhost_com@2x.png\r\n" +
            "1and1_fr.png\r\n" +
            "1and1_fr@2x.png\r\n" +
            "1euro_com.png\r\n" +
            "1euro_com@2x.png\r\n" +
            "1und1_de.png\r\n" +
            "1und1_de@2x.png\r\n";

    private final String mInvalidValidTestData = "000webhost_compng\r\n" +
            "000webhost_compng\r\n" +
            "1and1_frpng\r\n" +
            "1and1_frpng\r\n" +
            "1euro_compng\r\n" +
            "1euro_compng\r\n" +
            "1und1_depng\r\n" +
            "1und1_depng\r\n";

    private final long mPageId = 12313213;
    private IDataProvider mSpyDataProvider;
    private final MockWebServer mMockWebServer = new MockWebServer();
    private List<IWebsiteModel> mSampleValidList;

    @Mock
    private IAppConfig mMockConfig;

    @Mock
    private ICachePolicy mMockCachePolicy;

    @Before
    public void setup() {
        mSampleValidList = Collections.unmodifiableList(
                Arrays.asList(
                        getWebsiteModel("000webhost_com.png", "000webhost.com"),
                        getWebsiteModel("1and1_fr.png", "1and1.fr"),
                        getWebsiteModel("1euro_com.png", "1euro.com"),
                        getWebsiteModel("1und1_de.png", "1und1.de")
                ));
        MockitoAnnotations.initMocks(this);
        try {
            mMockWebServer.start();
        } catch (IOException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    @Test
    public void testValidListWithoutCache() {
        mMockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return new MockResponse().setBody(mValidTestData).setResponseCode(200);
            }
        });

        doReturn(false).when(mMockCachePolicy).isLruCacheEnabled();
        doReturn(mMockWebServer.url("/").toString()).when(mMockConfig).getEndpointsRootUrl();
        mSpyDataProvider = spy(new DataProvider.Builder(mMockConfig, RuntimeEnvironment.application.getApplicationContext())
                .cachePolicy(mMockCachePolicy)
                .setExecutorService(new MainThreadExecutorService())
                .build());
        final IDataProvider.IDataProviderListCallback<IWebsiteModel> callback = spy(new IDataProvider.IDataProviderListCallback<IWebsiteModel>() {
            @Override
            public void onSuccess(List<IWebsiteModel> response) {
                assertTrue(mSampleValidList.equals(response));
            }

            @Override
            public void onError(Exception e) {
                throw new AssertionError(e.getMessage());
            }
        });
        mSpyDataProvider.getWebsites(mPageId, callback);
        verify(callback).onSuccess(anyListOf(IWebsiteModel.class));
    }

    @Test
    public void testValidListWithCache() {
        mMockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return new MockResponse().setBody(mValidTestData).setResponseCode(200);
            }
        });

        doReturn(true).when(mMockCachePolicy).isLruCacheEnabled();
        doReturn(1024 * 1024 * 4).when(mMockCachePolicy).maxLruCacheSize(); // max lru cache set to 4MB
        doReturn(mMockWebServer.url("/").toString()).when(mMockConfig).getEndpointsRootUrl();
        mSpyDataProvider = spy(new DataProvider.Builder(mMockConfig, RuntimeEnvironment.application.getApplicationContext())
                .cachePolicy(mMockCachePolicy)
                .setExecutorService(new MainThreadExecutorService())
                .build());
        final TestResponseCallback callback = spy(new DataProviderTest.TestResponseCallback());
        final TestResponseCallback callback2 = spy(new DataProviderTest.TestResponseCallback());
        mSpyDataProvider.getWebsites(mPageId, callback);
        mSpyDataProvider.getWebsites(mPageId, callback2);
        verify(callback).onSuccess(anyListOf(IWebsiteModel.class));
        verify(callback2).onSuccess(anyListOf(IWebsiteModel.class));

        assertTrue(mSampleValidList.equals(callback.getResponse()));
        // assert that both responses are the same object
        assertTrue(mSampleValidList.equals(callback2.getResponse()));
        // assert that both responses are the same object
        assertTrue(callback.getResponse() == callback2.getResponse());
    }

    @Test
    public void testInvalidListWithoutCache() {
        mMockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return new MockResponse().setBody(mInvalidValidTestData).setResponseCode(200);
            }
        });

        doReturn(false).when(mMockCachePolicy).isLruCacheEnabled();
        doReturn(mMockWebServer.url("/").toString()).when(mMockConfig).getEndpointsRootUrl();
        mSpyDataProvider = spy(new DataProvider.Builder(mMockConfig, RuntimeEnvironment.application.getApplicationContext())
                .cachePolicy(mMockCachePolicy)
                .setExecutorService(new MainThreadExecutorService())
                .build());
        final IDataProvider.IDataProviderListCallback<IWebsiteModel> callback = spy(new IDataProvider.IDataProviderListCallback<IWebsiteModel>() {
            @Override
            public void onSuccess(List<IWebsiteModel> response) {
                assertTrue(response.size() == 0);
            }

            @Override
            public void onError(Exception e) {
                throw new AssertionError(e.getMessage());
            }
        });
        mSpyDataProvider.getWebsites(mPageId, callback);
        verify(callback).onSuccess(anyListOf(IWebsiteModel.class));
    }

    @Test
    public void testBadHttpResponse() {
        mMockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return new MockResponse().setResponseCode(404);
            }
        });

        doReturn(false).when(mMockCachePolicy).isLruCacheEnabled();
        doReturn(mMockWebServer.url("/").toString()).when(mMockConfig).getEndpointsRootUrl();
        mSpyDataProvider = spy(new DataProvider.Builder(mMockConfig, RuntimeEnvironment.application.getApplicationContext())
                .cachePolicy(mMockCachePolicy)
                .setExecutorService(new MainThreadExecutorService())
                .build());
        final IDataProvider.IDataProviderListCallback<IWebsiteModel> callback = spy(new IDataProvider.IDataProviderListCallback<IWebsiteModel>() {
            @Override
            public void onSuccess(List<IWebsiteModel> response) {
            }

            @Override
            public void onError(Exception e) {
            }
        });
        mSpyDataProvider.getWebsites(mPageId, callback);
        verify(callback).onError(any(Exception.class));
    }


    private IWebsiteModel getWebsiteModel(String logoId, String title) {
        return new WebsiteModel(logoId, title);
    }

    @After
    public void tearDown() {
        try {
            mMockWebServer.shutdown();
        } catch (IOException e) {
            throw new AssertionError(e.getMessage());
        }
    }


    public static class TestResponseCallback implements IDataProvider.IDataProviderListCallback<IWebsiteModel> {
        private List<IWebsiteModel> mResponse;

        @Override
        public void onSuccess(List<IWebsiteModel> response) {
            mResponse = response;
        }

        @Override
        public void onError(Exception e) {
            mResponse = null;
        }

        public List<IWebsiteModel> getResponse() {
            return mResponse;
        }
    }


}
