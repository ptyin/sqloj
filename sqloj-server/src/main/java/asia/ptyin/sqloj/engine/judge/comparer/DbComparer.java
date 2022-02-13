package asia.ptyin.sqloj.engine.judge.comparer;

import asia.ptyin.sqloj.engine.judge.JudgeResult;

public interface DbComparer
{
    JudgeResult compareTableMetadata(String tableA);
    JudgeResult compareTableData(String tableA);
    JudgeResult compareQueryOutput();
    JudgeResult compareGrantedPrivilege(String username);
}
