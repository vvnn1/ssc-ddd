package indi.melon.ssc.draft.domain.draft;

/**
 * @author wangmenglong
 * @since 2024/10/24 18:52
 */
public class DraftBuildUtil {
    public static Draft buildDraft(){
        Draft draft = new Draft(
                new DraftID("DraftID1"),
                "testName",
                DraftType.BATCH,
                "creator11"
        );
        draft.setContent("aaa");
        draft.setModifier("modifier11");
        return draft;
    }
}
