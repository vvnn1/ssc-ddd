package indi.melon.ssc.draft.domain.draft;

import indi.melon.ssc.draft.domain.version.Version;
import indi.melon.ssc.draft.domain.version.VersionID;

/**
 * @author wangmenglong
 * @since 2024/10/24 18:53
 */
public class VersionBuildUtil {
    public static Version buildVersion() {

        return new Version(
                new VersionID("VersionID1"),
                new DraftID("DraftID1"),
                "iamContent",
                "remark",
                "creator1"
        );
    }
}
